package it.univaq.disim.se4iot.sensorsimulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.se4iot.sensorsimulator.domain.FieldConfig;
import it.univaq.disim.se4iot.sensorsimulator.domain.SimulationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class MqttGateway {
    private final MqttPublisher mqttPublisher;
    private final ObjectMapper objectMapper;

    public MqttGateway(MqttPublisher mqttPublisher, ObjectMapper objectMapper) {
        this.mqttPublisher = mqttPublisher;
        this.objectMapper = objectMapper;
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationConfig")
    public void handleSimulationConfig(Message<?> message) {
        try {
            if (message.getPayload() instanceof byte[] bPayload) {
                String decodedPayload = new String(bPayload, StandardCharsets.UTF_8);
                log.info("Decoded payload: {}", decodedPayload);

                SimulationConfig config = objectMapper.readValue(decodedPayload, SimulationConfig.class);
                log.info("Configurazione ricevuta: Clima iniziale = {}", config.initialWeather());
                for (FieldConfig field : config.fields()) {
                    log.info("Campo agricolo configurato: {}", field);
                }
            }
        } catch (Exception e) {
            log.error("Failed to process simulation config: {}", e.getMessage(), e);
        }
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationUpdate")
    public void handleSimulationUpdate(String payload) {
        log.info("Handler Topic2: {}", payload);
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationStart")
    public void handleSimulationStart(String payload) {
        log.info("Handler Topic3: {}", payload);
    }

    @ServiceActivator(inputChannel = "inputChannelSimulationStop")
    public void handleSimulationStop(String payload) {
        log.info("Handler Topic4: {}", payload);
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleError(Message<?> errorMessage) {
        log.error("Errore nell'elaborazione del messaggio: {}", errorMessage);
    }
}
