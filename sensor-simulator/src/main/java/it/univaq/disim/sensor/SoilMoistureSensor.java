package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;
import it.univaq.disim.world.WeatherCondition;

public class SoilMoistureSensor extends AbstractSensor<Float> {
    public SoilMoistureSensor() {
        super("Soil-Moisture", "%");
    }

    public SoilMoistureSensor(String name) {
        super(name, "%");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.getWeatherCondition();
        float currentValue = getValue() != null ? getValue() : 50.0f; // Valore iniziale
        switch (condition) {
            case LIGHT_RAIN -> currentValue += (float) (Math.random() * 5); // Piccolo aumento
            case MODERATE_RAIN -> currentValue += (float) (Math.random() * 10 + 5); // Aumento moderato
            case HEAVY_RAIN, HURRICANE -> currentValue += (float) (Math.random() * 20 + 10); // Aumento significativo
            case SUNNY -> currentValue -= (float) (Math.random() * 3); // Leggera diminuzione
            case CLOUDY -> currentValue -= (float) (Math.random() * 1); // Diminuzione quasi impercettibile
        }
        currentValue = Math.clamp(currentValue, 0.0f, 100.0f); // Limita il valore tra 0 e 100
        setValue(currentValue);
        return currentValue;
    }
}
