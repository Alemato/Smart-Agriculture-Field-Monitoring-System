package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

import java.util.List;

public record CurrentSimulationCondition(
        @JsonProperty(value = "weather", required = true) WeatherCondition weather,
        @JsonProperty(value = "temperature", required = true) float externalTemperature,
        @JsonProperty(value = "humidity", required = true)float relativeHumidity,
        @JsonProperty(value = "fields", required = true) List<FieldConfig> fields,
        @JsonProperty("interval") Integer interval
) {
}
