package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;
import it.univaq.disim.world.WeatherCondition;

public class TemperatureSensor extends AbstractSensor<Float> {
    public TemperatureSensor() {
        super("Temperature", "°C");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.getWeatherCondition();
        float adjustment = switch (condition) {
            case SUNNY -> (float) Math.random() * 2 + 1; // Aumento della temperatura
            case CLOUDY -> 0; // Nessun cambiamento significativo
            case LIGHT_RAIN, MODERATE_RAIN -> -(float) Math.random() * 2 - 1; // Leggera diminuzione
            case HEAVY_RAIN, HURRICANE -> -(float) Math.random() * 5 - 2; // Forte diminuzione
        };
        float temperature = context.getExternalTemperature() + adjustment;
        temperature = Math.clamp(temperature, -10.0f, 40.0f); // Limita la temperatura tra -10°C e 40°C
        setValue(temperature);
        return temperature;
    }
}
