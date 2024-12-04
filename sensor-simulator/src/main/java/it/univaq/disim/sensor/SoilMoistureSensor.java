package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;

public class SoilMoistureSensor extends AbstractSensor<Float> {
    public SoilMoistureSensor() {
        super("Soil-Moisture", "%");
    }

    public SoilMoistureSensor(String name) {
        super(name, "%");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        float currentValue = getValue() != null ? getValue() : 50.0f; // Valore iniziale
        if (context.isRaining()) {
            currentValue += (float) Math.random() * 10; // Aumenta umidità in caso di pioggia
        } else {
            currentValue -= (float) Math.random() * 5; // Riduce l'umidità gradualmente
        }
        currentValue = Math.clamp(currentValue, 0.0f, 100.0f); // Limita il valore tra 0 e 100
        setValue(currentValue);
        return currentValue;
    }
}
