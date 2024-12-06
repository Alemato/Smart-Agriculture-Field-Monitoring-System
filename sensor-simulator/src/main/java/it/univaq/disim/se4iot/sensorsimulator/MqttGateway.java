package it.univaq.disim.se4iot.sensorsimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttGateway {
    @ServiceActivator(inputChannel = "inputChannelTopic1")
    public void handleTopic1(String payload) {
        log.info("Handler Topic1: {}", payload);
    }

    @ServiceActivator(inputChannel = "inputChannelTopic2")
    public void handleTopic2(String payload) {
        log.info("Handler Topic2: {}", payload);
    }

    @ServiceActivator(inputChannel = "inputChannelTopic3")
    public void handleTopic3(String payload) {
        log.info("Handler Topic3: {}", payload);
    }

    @ServiceActivator(inputChannel = "inputChannelTopic4")
    public void handleTopic4(String payload) {
        log.info("Handler Topic4: {}", payload);
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleError(Message<?> errorMessage) {
        log.error("Errore nell'elaborazione del messaggio: {}", errorMessage);
    }
}
