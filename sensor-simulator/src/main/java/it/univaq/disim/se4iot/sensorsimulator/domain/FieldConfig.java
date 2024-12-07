package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record FieldConfig(
        @JsonProperty("fieldId") int fieldId,
        @JsonProperty("sensors") Map<String, Boolean> sensors,
        @JsonProperty("interval") int interval) {
}
