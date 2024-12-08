package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SensorsConfig(
        @JsonProperty("soilMoisture") boolean soilMoisture,
        @JsonProperty("temperature") boolean temperature,
        @JsonProperty("ph") boolean ph,
        @JsonProperty("salinity") boolean salinity,
        @JsonProperty("humidity") boolean humidity,
        @JsonProperty("rain") boolean rain
) {
}
