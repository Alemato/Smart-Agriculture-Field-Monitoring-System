package it.univaq.disim.se4iot.sensorsimulator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private String brokerUrl;
    private String clientId;
    private String username;
    private String password;
}
