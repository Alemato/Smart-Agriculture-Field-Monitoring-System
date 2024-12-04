package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;

public class RelativeHumiditySensor extends AbstractSensor<Float> {
    public RelativeHumiditySensor() {
        super("Relative-Humidity", "%");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        float adjustment = (float) (context.isRaining() ? Math.random() * 10 + 5 : Math.random() * -5); // Aumenta in caso di pioggia
        float humidity = context.getRelativeHumidity() + adjustment;
        humidity = Math.clamp(humidity, 0.0f, 100.0f); // Limita tra 0% e 100%
        setValue(humidity);
        return humidity;
    }
}
