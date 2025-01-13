package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class SoilMoistureSensor extends AbstractSensor<Float> {
    public SoilMoistureSensor() {
        super("Soil-Moisture", "%");
    }

    public SoilMoistureSensor(String name) {
        super(name, "%");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.weatherCondition();
        float currentValue = getValue() != null ? getValue() : 50.0f; // Valore iniziale
        float adjustment = switch (condition) {
            case LIGHT_RAIN -> (float) (Math.random() * 5); // Piccolo aumento
            case MODERATE_RAIN -> (float) (Math.random() * 10 + 5); // Aumento moderato
            case HEAVY_RAIN, HURRICANE -> (float) (Math.random() * 20 + 10); // Aumento significativo
            case SUNNY -> (float) (Math.random() * 3) * -1; // Leggera diminuzione
            case CLOUDY -> (float) (Math.random() * 1) * -1; // Diminuzione quasi impercettibile
        };
        // Genera un segno random (+1 o -1)
        float sign = (Math.random() < 0.5) ? 1f : -1f;
        currentValue = currentValue + (sign * adjustment);
        currentValue = Math.clamp(currentValue, 0.0f, 100.0f); // Limita il valore tra 0 e 100
        setValue(currentValue);
        return currentValue;
    }
}
