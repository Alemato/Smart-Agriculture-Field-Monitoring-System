package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;
import it.univaq.disim.world.WeatherCondition;

public class SoilPHSensor extends AbstractSensor<Float> {
    public SoilPHSensor() {
        super("Soil-pH", "");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.getWeatherCondition();
        float currentValue = getValue() != null ? getValue() : 6.5f; // Valore medio
        switch (condition) {
            case LIGHT_RAIN, MODERATE_RAIN -> currentValue -= (float) (Math.random() * 0.1); // Lieve acidificazione
            case HEAVY_RAIN, HURRICANE -> currentValue -= (float) (Math.random() * 0.2); // Maggiore acidificazione
            default -> currentValue += (float) (Math.random() * 0.05 - 0.025); // Leggera oscillazione
        }
        currentValue = Math.clamp(currentValue, 4.0f, 8.5f); // Limita il pH tra 4.0 e 8.5
        setValue(currentValue);
        return currentValue;
    }
}
