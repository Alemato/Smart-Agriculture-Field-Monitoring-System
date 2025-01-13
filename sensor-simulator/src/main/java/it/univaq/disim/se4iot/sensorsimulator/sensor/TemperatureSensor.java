package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class TemperatureSensor extends AbstractSensor<Float> {
    public TemperatureSensor() {
        super("Temperature", "°C");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.weatherCondition();
        float adjustment = switch (condition) {
            case SUNNY -> (float) Math.random() * 1 + 1; // Aumento della temperatura
            case CLOUDY -> 0; // Nessun cambiamento significativo
            case LIGHT_RAIN, MODERATE_RAIN -> -(float) Math.random() * 2 - 1; // Leggera diminuzione
            case HEAVY_RAIN, HURRICANE -> -(float) Math.random() * 4 - 2; // Forte diminuzione
        };
        // Genera un segno random (+1 o -1)
        float sign = (Math.random() < 0.5) ? 1f : -1f;

        float temperature = context.externalTemperature() + (sign * adjustment);
        temperature = Math.clamp(temperature, -10.0f, 40.0f); // Limita la temperatura tra -10°C e 40°C
        setValue(temperature);
        return temperature;
    }
}
