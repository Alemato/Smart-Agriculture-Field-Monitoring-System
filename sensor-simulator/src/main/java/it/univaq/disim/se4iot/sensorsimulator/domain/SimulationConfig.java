package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

import java.util.List;

public record SimulationConfig(
        @JsonProperty(value = "initialWeather", required = true) WeatherCondition initialWeather,
        @JsonProperty(value = "fields", required = true) List<FieldConfig> fields,
        @JsonProperty(value = "interval", required = true) int interval) {
}
