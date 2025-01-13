package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class SoilPHSensor extends AbstractSensor<Float> {
    public SoilPHSensor() {
        super("Soil-pH", "");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.weatherCondition();
        float currentValue = getValue() != null ? getValue() : 6.5f; // Valore medio
        float adjustment = switch (condition) {
            case LIGHT_RAIN, MODERATE_RAIN -> (float) (Math.random() * 0.1) * -1; // Lieve acidificazione
            case HEAVY_RAIN, HURRICANE -> (float) (Math.random() * 0.2) * -1; // Maggiore acidificazione
            default -> (float) (Math.random() * 0.05 - 0.025); // Leggera oscillazione
        };
        // Genera un segno random (+1 o -1)
        float sign = (Math.random() < 0.5) ? 1f : -1f;
        currentValue = currentValue + (sign * adjustment);
        currentValue = Math.clamp(currentValue, 4.0f, 8.5f); // Limita il pH tra 4.0 e 8.5
        setValue(currentValue);
        return currentValue;
    }
}
