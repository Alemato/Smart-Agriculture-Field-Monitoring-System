package it.univaq.disim.se4iot.sensorsimulator.world;

public record ClimateContext(
        WeatherCondition weatherCondition,
        float externalTemperature,
        float relativeHumidity
) {
    public boolean isRaining() {
        return !weatherCondition.equals(WeatherCondition.SUNNY) && !weatherCondition.equals(WeatherCondition.CLOUDY);
    }
}
