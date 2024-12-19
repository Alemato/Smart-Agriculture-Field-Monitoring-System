package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SensorsConfig(
        @JsonProperty(value = "soilMoisture", required = true) boolean soilMoisture,
        @JsonProperty(value = "temperature", required = true) boolean temperature,
        @JsonProperty(value = "ph", required = true) boolean ph,
        @JsonProperty(value = "salinity", required = true) boolean salinity,
        @JsonProperty(value = "humidity", required = true) boolean humidity,
        @JsonProperty(value = "rain", required = true) boolean rain
) {
}
