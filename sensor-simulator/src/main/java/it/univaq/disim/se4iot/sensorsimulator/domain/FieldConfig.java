package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FieldConfig(
        @JsonProperty(value = "fieldId", required = true) int fieldId,
        @JsonProperty(value = "sensors", required = true) SensorsConfig sensors) {
}
