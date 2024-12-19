package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class RainfallSensor extends AbstractSensor<Float> {
    public RainfallSensor() {
        super("Rain-Detection", "mm");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.weatherCondition();
        float rainfall = switch (condition) {
            case LIGHT_RAIN -> (float) (Math.random() * 2 + 1); // 1-3 mm
            case MODERATE_RAIN -> (float) (Math.random() * 5 + 3); // 3-8 mm
            case HEAVY_RAIN -> (float) (Math.random() * 10 + 8); // 8-18 mm
            case HURRICANE -> (float) (Math.random() * 50 + 20); // 20-70 mm
            default -> 0.0f; // Nessuna pioggia per SUNNY o CLOUDY
        };

        setValue(rainfall);
        return rainfall;
    }
}
