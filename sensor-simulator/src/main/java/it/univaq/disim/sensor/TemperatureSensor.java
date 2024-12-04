package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;

public class TemperatureSensor extends AbstractSensor<Float> {
    public TemperatureSensor() {
        super("Temperature", "°C");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        float adjustment = context.isRaining() ? -2.0f : 0.0f; // Riduzione della temperatura in caso di pioggia
        float temperature = context.getExternalTemperature() + adjustment + (float) (Math.random() * 2 - 1); // Oscilla leggermente
        temperature = Math.clamp(temperature, -10.0f, 40.0f); // Limita tra -10°C e 40°C
        setValue(temperature);
        return temperature;
    }
}
