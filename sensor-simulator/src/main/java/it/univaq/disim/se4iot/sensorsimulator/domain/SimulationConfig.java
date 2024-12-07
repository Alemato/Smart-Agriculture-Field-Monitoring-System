package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

import java.util.List;

public record SimulationConfig(
        @JsonProperty("initialWeather") WeatherCondition initialWeather,
        @JsonProperty("fields") List<FieldConfig> fields) {
}
