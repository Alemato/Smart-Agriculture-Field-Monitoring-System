package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class RelativeHumiditySensor extends AbstractSensor<Float> {
    public RelativeHumiditySensor() {
        super("Relative-Humidity", "%");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.weatherCondition();
        float humidity = context.relativeHumidity();
        float adjustment = switch (condition) {
            case SUNNY -> (float) (Math.random() * 5) * -1; // Diminuzione
            case CLOUDY -> (float) (Math.random() * 1); // Leggero aumento
            case LIGHT_RAIN -> (float) (Math.random() * 5); // Aumento
            case MODERATE_RAIN -> (float) (Math.random() * 10); // Aumento moderato
            case HEAVY_RAIN, HURRICANE -> (float) (Math.random() * 20); // Aumento significativo
        };
        // Genera un segno random (+1 o -1)
        float sign = (Math.random() < 0.5) ? 1f : -1f;
        humidity = humidity + (sign * adjustment);
        humidity = Math.clamp(humidity, 0.0f, 100.0f); // Limita tra 0% e 100%
        setValue(humidity);
        return humidity;
    }
}
