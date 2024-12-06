package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class RelativeHumiditySensor extends AbstractSensor<Float> {
    public RelativeHumiditySensor() {
        super("Relative-Humidity", "%");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.getWeatherCondition();
        float humidity = context.getRelativeHumidity();
        switch (condition) {
            case SUNNY -> humidity -= (float) (Math.random() * 5); // Diminuzione
            case CLOUDY -> humidity += (float) (Math.random() * 1); // Leggero aumento
            case LIGHT_RAIN -> humidity += (float) (Math.random() * 5); // Aumento
            case MODERATE_RAIN -> humidity += (float) (Math.random() * 10); // Aumento moderato
            case HEAVY_RAIN, HURRICANE -> humidity += (float) (Math.random() * 20); // Aumento significativo
        }
        humidity = Math.clamp(humidity, 0.0f, 100.0f); // Limita tra 0% e 100%
        setValue(humidity);
        return humidity;
    }
}
