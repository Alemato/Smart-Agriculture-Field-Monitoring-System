package it.univaq.disim.se4iot.sensorsimulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.se4iot.sensorsimulator.domain.CurrentSimulationCondition;
import it.univaq.disim.se4iot.sensorsimulator.domain.SimulationConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.UpdateSimulationCondition;
import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MqttGateway {
    private final MqttPublisher mqttPublisher;
    private final ObjectMapper objectMapper;
    private final Simulation simulation;
    private static final String FAIL_PROCESS_MESSAGE = "Failed to process simulation: {}";

    public MqttGateway(MqttPublisher mqttPublisher, ObjectMapper objectMapper, Simulation simulation) {
        this.mqttPublisher = mqttPublisher;
        this.objectMapper = objectMapper;
        this.simulation = simulation;
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationConfig")
    public void handleSimulationConfig(Message<?> message) {
        log.info("Ricevuto Messaggio da sensors/simulation/config");
        try {
            if (message.getPayload() instanceof byte[] bPayload) {
                String decodedPayload = new String(bPayload, StandardCharsets.UTF_8);
                SimulationConfig config = objectMapper.readValue(decodedPayload, SimulationConfig.class);
                log.info("Configurazione Ricevuta: {}", config);
                simulation.changeConfiguration(config);
            }
        } catch (Exception e) {
            log.error(FAIL_PROCESS_MESSAGE, e.getMessage(), e);
        }
    }

    @ServiceActivator(inputChannel = "inputChannelGetSimulationConfig")
    public void handleGetSimulationUpdate(String payload) {
        log.info("Ricevuto Messaggio da sensors/simulation/config/get: {}", payload);
        try {
            SimulationConfig config = simulation.getConfig();
            ClimateContext climateContext = simulation.getClimateContext();
            CurrentSimulationCondition currentSimulationCondition = new CurrentSimulationCondition(climateContext.weatherCondition(), climateContext.externalTemperature(), climateContext.relativeHumidity(), config.fields(), config.interval());
            mqttPublisher.publish("sensors/simulation/config/current", objectMapper.writeValueAsString(currentSimulationCondition));
            log.info("Invio Messaggio sensors/simulation/config/current: {}", currentSimulationCondition);
        } catch (Exception e) {
            log.error(FAIL_PROCESS_MESSAGE, e.getMessage(), e);
        }
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationUpdate")
    public void handleSimulationUpdate(Message<?> message) {
        log.info("Ricevuto Messaggio da sensors/simulation/update");
        try {
            if (message.getPayload() instanceof byte[] bPayload) {
                String decodedPayload = new String(bPayload, StandardCharsets.UTF_8);
                UpdateSimulationCondition updateSimulationCondition = objectMapper.readValue(decodedPayload, UpdateSimulationCondition.class);
                log.info("Aggiornamento Condizioni di Simulazione Ricevuta: {}", updateSimulationCondition);
                simulation.updateSimulation(updateSimulationCondition);
            }
        } catch (Exception e) {
            log.error(FAIL_PROCESS_MESSAGE, e.getMessage(), e);
        }
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationStart")
    public void handleSimulationStart(Message<?> message) {
        log.info("Ricevuto Messaggio da sensors/simulation/start");
        simulation.startSimulation();
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationStop")
    public void handleSimulationStop(Message<?> message) {
        log.info("Ricevuto Messaggio da sensors/simulation/stop");
        simulation.stopSimulation();
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleError(Message<?> errorMessage) {
        log.error("Errore nell'elaborazione del messaggio: {}", errorMessage);
    }
}
