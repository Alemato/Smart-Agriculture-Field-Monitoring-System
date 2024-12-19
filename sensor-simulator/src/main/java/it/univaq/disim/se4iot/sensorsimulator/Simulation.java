package it.univaq.disim.se4iot.sensorsimulator;

import it.univaq.disim.se4iot.sensorsimulator.domain.FieldConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.SensorsConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.SimulationConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.UpdateSimulationCondition;
import it.univaq.disim.se4iot.sensorsimulator.sensor.*;
import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class Simulation {

    public static final int DEFAULT_INTERVAL_SIMULATION = 5000;
    public static final String SOIL_MOISTURE_SENSOR = "soilMoisture";
    public static final String TEMPERATURE_SENSOR = "temperature";
    public static final String PH_SENSOR = "ph";
    public static final String SALINITY_SENSOR = "salinity";
    public static final String HUMIDITY_SENSOR = "humidity";
    public static final String RAIN_SENSOR = "rain";

    @Getter
    private volatile SimulationConfig config;
    @Getter
    private volatile ClimateContext climateContext;

    private final AtomicBoolean simulationStarted = new AtomicBoolean(false);
    private final MqttPublisher mqttPublisher;
    private final ScheduledExecutorService scheduler;
    private final Map<Integer, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

    public Simulation(MqttPublisher mqttPublisher) {
        this.mqttPublisher = mqttPublisher;
        this.scheduler = Executors.newScheduledThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        this.climateContext = new ClimateContext(WeatherCondition.SUNNY, 24.5f, 60.5f);
        this.config = initializeDefaultSimulation();
    }

    private SimulationConfig initializeDefaultSimulation() {
        final SensorsConfig sensorsConfig = new SensorsConfig(true, true, true, true, true, true);
        final FieldConfig field1 = new FieldConfig(1, sensorsConfig);
        final FieldConfig field2 = new FieldConfig(2, sensorsConfig);
        return new SimulationConfig(climateContext.weatherCondition(), List.of(field1, field2), DEFAULT_INTERVAL_SIMULATION);
    }

    public void startSimulation() {
        if (simulationStarted.compareAndSet(false, true)) {
            // Pianifica i task per ogni campo in base all'intervallo attuale
            for (FieldConfig field : config.fields()) {
                scheduleFieldTask(field, config.interval());
            }
            log.info("Simulazione avviata con successo.");
        } else {
            log.warn("La simulazione è già in corso!");
        }
    }

    private void scheduleFieldTask(FieldConfig field, int interval) {
        final ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(
                () -> simulateFieldMeasurement(field),
                0,
                interval,
                TimeUnit.MILLISECONDS
        );
        taskMap.put(field.fieldId(), task);
    }

    private void simulateFieldMeasurement(FieldConfig field) {
        final ClimateContext localContext = climateContext; // Lettura volatile
        log.debug("Simulazione in corso per il campo {} con condizioni climatiche: {}",
                field.fieldId(), localContext.weatherCondition());

        final SensorsConfig sensors = field.sensors();
        try {
            // Esegue la lettura dei sensori abilitati
            if (sensors.soilMoisture())
                sendMeasurement(field.fieldId(), SOIL_MOISTURE_SENSOR, new SoilMoistureSensor().getMeasurement(localContext).toString());
            if (sensors.temperature())
                sendMeasurement(field.fieldId(), TEMPERATURE_SENSOR, new TemperatureSensor().getMeasurement(localContext).toString());
            if (sensors.ph())
                sendMeasurement(field.fieldId(), PH_SENSOR, new SoilPHSensor().getMeasurement(localContext).toString());
            if (sensors.salinity())
                sendMeasurement(field.fieldId(), SALINITY_SENSOR, new WaterSalinitySensor().getMeasurement(localContext).toString());
            if (sensors.humidity())
                sendMeasurement(field.fieldId(), HUMIDITY_SENSOR, new RelativeHumiditySensor().getMeasurement(localContext).toString());
            if (sensors.rain())
                sendMeasurement(field.fieldId(), RAIN_SENSOR, new RainfallSensor().getMeasurement(localContext).toString());
        } catch (Exception e) {
            log.error("Errore durante la simulazione per il campo {}: {}", field.fieldId(), e.getMessage(), e);
        }
    }

    private void sendMeasurement(int fieldId, String sensorType, String measurement) {
        mqttPublisher.publish("agriculture/field" + fieldId + "/" + sensorType, measurement);
        log.info("Dato inviato via MQTT: campo {}, sensore {}, valore: {}", fieldId, sensorType, measurement);
    }

    /**
     * Aggiorna il clima globale e l'intervallo globale. Il clima resta unico per tutti i campi.
     * Se l'intervallo cambia, i task vengono ricreati; in caso contrario, restano invariati.
     * @param simulationConditions condizioni aggiornate
     */
    public void updateSimulation(UpdateSimulationCondition simulationConditions) {
        if (!simulationStarted.get()) {
            log.warn("La simulazione non è in corso. Non è possibile aggiornare i settaggi.");
            return;
        }

        final ClimateContext oldContext = climateContext;

        // Aggiorno il clima con eventuali valori nuovi, altrimenti uso quelli vecchi
        final ClimateContext newClimate = new ClimateContext(
                simulationConditions.weather() != null ? simulationConditions.weather() : oldContext.weatherCondition(),
                simulationConditions.externalTemperature() != null ? simulationConditions.externalTemperature() : oldContext.externalTemperature(),
                simulationConditions.relativeHumidity() != null ? simulationConditions.relativeHumidity() : oldContext.relativeHumidity()
        );

        final int oldInterval = config.interval();
        final int newInterval = simulationConditions.interval() != null ? simulationConditions.interval() : oldInterval;

        // Creo una nuova config immutabile con le stesse fields e il nuovo (o vecchio) intervallo
        final SimulationConfig newConfig = new SimulationConfig(newClimate.weatherCondition(), config.fields(), newInterval);

        // Aggiorno gli stati volatili
        this.climateContext = newClimate;
        this.config = newConfig;

        // Ricreo i task solo se l'intervallo è cambiato
        rescheduleTasksIfNeeded(oldInterval, newInterval);

        log.info("Settaggi aggiornati: clima {}, intervallo {} ms.", newClimate.weatherCondition(), newInterval);
    }

    /**
     * Se l'intervallo cambia, ferma tutti i task e li ricrea con il nuovo intervallo.
     */
    private void rescheduleTasksIfNeeded(int oldInterval, int newInterval) {
        if (newInterval != oldInterval) {
            stopTasks(); // Cancella tutti i task correnti
            // Ricrea i task con il nuovo intervallo
            for (FieldConfig field : config.fields()) {
                scheduleFieldTask(field, newInterval);
            }
            log.debug("Task rischedulati con intervallo aggiornato a {} ms.", newInterval);
        } else {
            log.debug("L'intervallo non è cambiato, nessuna rischedulazione dei task necessaria.");
        }
    }

    /**
     * Cambia l'intera configurazione. Se la simulazione era in corso, la ferma,
     * aggiorna il clima con la nuova condizione meteo e poi riparte.
     */
    public void changeConfiguration(SimulationConfig newConfig) {
        final boolean wasRunning = simulationStarted.compareAndSet(true, false);
        if (wasRunning) {
            stopTasks();
        }

        // Aggiorno il clima mantenendo T e umidità dal vecchio contesto
        final ClimateContext oldContext = climateContext;
        final ClimateContext newClimate = new ClimateContext(
                newConfig.initialWeather(),
                oldContext.externalTemperature(),
                oldContext.relativeHumidity()
        );

        this.climateContext = newClimate;
        this.config = newConfig;
        log.info("Configurazione cambiata: {}", newConfig);

        // Se era in esecuzione, riparti
        if (wasRunning) {
            startSimulation();
        }
    }

    public void stopSimulation() {
        if (simulationStarted.compareAndSet(true, false)) {
            stopTasks();
            log.info("Simulazione fermata.");
        } else {
            log.warn("La simulazione non è in corso.");
        }
    }

    /**
     * Cancella tutti i task attualmente programmati.
     */
    private void stopTasks() {
        for (ScheduledFuture<?> task : taskMap.values()) {
            task.cancel(false);
        }
        taskMap.clear();
    }

    /**
     * Metodi per eventuale shutdown del pool di thread se necessario.
     * Chiamare nel contesto corretto se si vuole fermare l'intera applicazione.
     */
    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
