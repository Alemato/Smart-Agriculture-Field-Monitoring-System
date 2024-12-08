package it.univaq.disim.se4iot.sensorsimulator;

import it.univaq.disim.se4iot.sensorsimulator.domain.FieldConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.SensorsConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.SimulationConfig;
import it.univaq.disim.se4iot.sensorsimulator.sensor.*;
import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Data
@Component
public class Simulation {
    public static final int DEFAULT_INTERVAL_SIMULATION = 5000;
    public static final String SOIL_MOISTURE_SENSOR = "soilMoisture";
    public static final String TEMPERATURE_SENSOR = "temperature";
    public static final String PH_SENSOR = "ph";
    public static final String SALINITY_SENSOR = "salinity";
    public static final String HUMIDITY_SENSOR = "humidity";
    public static final String RAIN_SENSOR = "rain";

    // Config globale immutabile
    private volatile SimulationConfig config;
    // Clima unico per tutti i campi
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
        SensorsConfig sensorsConfig = new SensorsConfig(true, true, true, true, true, true);
        FieldConfig field1 = new FieldConfig(1, sensorsConfig, DEFAULT_INTERVAL_SIMULATION);
        FieldConfig field2 = new FieldConfig(2, sensorsConfig, DEFAULT_INTERVAL_SIMULATION);
        return new SimulationConfig(climateContext.getWeatherCondition(), List.of(field1, field2));
    }

    public void startSimulation() {
        if (simulationStarted.compareAndSet(false, true)) {
            for (FieldConfig field : config.fields()) {
                scheduleFieldTask(field);
            }
            log.info("Simulazione avviata con successo.");
        } else {
            log.warn("La simulazione è già in corso!");
        }
    }

    private void scheduleFieldTask(FieldConfig field) {
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(
                () -> simulateFieldMeasurement(field),
                0,
                field.interval(),
                TimeUnit.MILLISECONDS
        );
        taskMap.put(field.fieldId(), task);
    }

    private void simulateFieldMeasurement(FieldConfig field) {
        ClimateContext localContext = climateContext; // Lettura volatile
        log.info("Simulazione in corso per il campo {} con condizioni climatiche: {}",
                field.fieldId(), localContext.getWeatherCondition());

        SensorsConfig sensors = field.sensors();
        try {
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
            log.error("Errore durante la simulazione per il campo {}: {}", field.fieldId(), e.getMessage());
        }
    }

    private void sendMeasurement(int fieldId, String sensorType, String measurement) {
        mqttPublisher.publish("test/field" + fieldId + "/" + sensorType, measurement);
        log.info("Dato inviato via MQTT: campo {}, sensore {}, valore: {}", fieldId, sensorType, measurement);
    }

    /**
     * Aggiorna il clima globale e, se specificato, l'intervallo di un singolo campo.
     * Il clima è unico, quindi l'aggiornamento influisce su tutti i campi, ma
     * l'intervallo cambiato riguarda solo il `fieldId` selezionato.
     */
    public void updateSimulation(WeatherCondition weatherCondition, int interval, int fieldId) {
        if (!simulationStarted.get()) {
            log.warn("La simulazione non è in corso. Non è possibile aggiornare i settaggi.");
            return;
        }

        // Aggiorno il clima globale in modo immutabile
        ClimateContext oldContext = climateContext;
        this.climateContext = new ClimateContext(
                weatherCondition,
                oldContext.getExternalTemperature(),
                oldContext.getRelativeHumidity()
        );

        // Aggiorno la config con il nuovo intervallo per il campo specificato
        List<FieldConfig> updatedFields = config.fields().stream()
                .map(field -> field.fieldId() == fieldId
                        ? new FieldConfig(field.fieldId(), field.sensors(), interval)
                        : field)
                .toList();

        this.config = new SimulationConfig(this.climateContext.getWeatherCondition(), updatedFields);

        // Aggiorno la schedulazione solo per il campo interessato
        ScheduledFuture<?> oldTask = taskMap.remove(fieldId);
        if (oldTask != null) {
            oldTask.cancel(false);
        }

        // Ricreo il task con il nuovo intervallo
        updatedFields.stream()
                .filter(field -> field.fieldId() == fieldId)
                .findFirst()
                .ifPresent(this::scheduleFieldTask);

        log.info("Settaggi aggiornati: condizione meteo globale {}, intervallo per campo {} a {} ms.",
                weatherCondition, fieldId, interval);
    }

    /**
     * Cambia l'intera configurazione. Se la simulazione era in corso, la ferma,
     * cambia la config, aggiorna il clima e poi riparte.
     */
    public void changeConfiguration(SimulationConfig newConfig) {
        boolean wasRunning = simulationStarted.compareAndSet(true, false);
        if (wasRunning) {
            stopTasks();
        }

        // Manteniamo stessi valori di temperatura e umidità, ma aggiorniamo il clima con la nuova condizione meteo
        ClimateContext oldContext = climateContext;
        this.climateContext = new ClimateContext(
                newConfig.initialWeather(),
                oldContext.getExternalTemperature(),
                oldContext.getRelativeHumidity()
        );

        this.config = newConfig;
        log.info("Configurazione cambiata: {}", newConfig);

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

    private void stopTasks() {
        for (ScheduledFuture<?> task : taskMap.values()) {
            task.cancel(false);
        }
        taskMap.clear();
    }
}
