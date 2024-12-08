package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FieldConfig(
        @JsonProperty("fieldId") int fieldId,
        @JsonProperty("sensors") SensorsConfig sensors,
        @JsonProperty("interval") int interval) {
}
