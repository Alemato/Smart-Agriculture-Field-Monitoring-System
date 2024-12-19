package it.univaq.disim.se4iot.sensorsimulator.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.Mqttv5ClientManager;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class MqttConfiguration {
    private final MqttProperties mqttProperties;

    public MqttConfiguration(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Bean
    public MqttConnectionOptions mqttConnectionOptions() {
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setServerURIs(new String[]{mqttProperties.getBrokerUrl()});
        options.setAutomaticReconnect(true);
        options.setCleanStart(true); // Usare per mantenere lo stato delle sottoscrizioni
        return options;
    }

    @Bean
    public MqttAsyncClient mqttClient() throws Exception {
        return new MqttAsyncClient(mqttProperties.getBrokerUrl(), mqttProperties.getClientId());
    }

    @Bean
    public Mqttv5ClientManager mqttv5ClientManager() {
        return new Mqttv5ClientManager(mqttConnectionOptions(), mqttProperties.getClientId());
    }

    // Canale principale per i messaggi in ingresso
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel errorChannel() {
        return new DirectChannel();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // Adapter per sottoscriversi ai topic in ingresso
    @Bean
    public MessageProducer inbound() {
        Mqttv5PahoMessageDrivenChannelAdapter adapter = new Mqttv5PahoMessageDrivenChannelAdapter(
                mqttv5ClientManager(),
                "sensors/simulation/config", "sensors/simulation/config/get", "sensors/simulation/update", "sensors/simulation/start", "sensors/simulation/stop"
        );
        adapter.setOutputChannel(mqttInputChannel());
        adapter.setErrorChannel(errorChannel());
        return adapter;
    }

    // Router per separare i messaggi per topic
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public HeaderValueRouter router() {
        HeaderValueRouter router = new HeaderValueRouter("mqtt_receivedTopic");
        router.setChannelMapping("sensors/simulation/config/get", "inputChannelGetSimulationConfig");
        router.setChannelMapping("sensors/simulation/config", "inputChannelSimulationConfig");
        router.setChannelMapping("sensors/simulation/update", "inputChannelSimulationUpdate");
        router.setChannelMapping("sensors/simulation/start", "inputChannelSimulationStart");
        router.setChannelMapping("sensors/simulation/stop", "inputChannelSimulationStop");
        return router;
    }

    // Canali separati per ogni topic
    @Bean
    public MessageChannel inputChannelSimulationConfig() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inputChannelGetSimulationConfig() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inputChannelSimulationUpdate() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inputChannelSimulationStart() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inputChannelSimulationStop() {
        return new DirectChannel();
    }

    // Canale per inviare messaggi in uscita
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // Handler per pubblicare i messaggi sui topic in uscita
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        Mqttv5PahoMessageHandler handler = new Mqttv5PahoMessageHandler(mqttv5ClientManager());
        handler.setAsync(true);
        return handler;
    }
}
