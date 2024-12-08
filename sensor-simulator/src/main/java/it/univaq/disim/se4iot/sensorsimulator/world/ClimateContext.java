package it.univaq.disim.se4iot.sensorsimulator.world;

import lombok.Data;

@Data
public class ClimateContext {
    private WeatherCondition weatherCondition;
    private boolean isRaining;
    private float externalTemperature; // Temperatura esterna
    private float relativeHumidity;    // Umidità relativa

    public ClimateContext(WeatherCondition weatherCondition, float externalTemperature, float relativeHumidity) {
        this.weatherCondition = weatherCondition;
        this.isRaining = !weatherCondition.equals(WeatherCondition.SUNNY) && !weatherCondition.equals(WeatherCondition.CLOUDY);
        this.externalTemperature = externalTemperature;
        this.relativeHumidity = relativeHumidity;
    }

    public boolean isRaining() {
        return !weatherCondition.equals(WeatherCondition.SUNNY) && !weatherCondition.equals(WeatherCondition.CLOUDY);
    }
}
