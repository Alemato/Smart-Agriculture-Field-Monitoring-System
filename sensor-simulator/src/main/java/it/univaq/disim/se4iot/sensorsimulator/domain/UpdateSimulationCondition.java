package it.univaq.disim.se4iot.sensorsimulator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public record UpdateSimulationCondition(
        @JsonProperty("weather") WeatherCondition weather,
        @JsonProperty("interval") Integer interval,
        @JsonProperty("temperature") Float externalTemperature,
        @JsonProperty("humidity")Float relativeHumidity
) {
}
