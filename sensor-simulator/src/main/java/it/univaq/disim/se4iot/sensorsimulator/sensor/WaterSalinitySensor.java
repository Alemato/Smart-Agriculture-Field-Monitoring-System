package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class WaterSalinitySensor extends AbstractSensor<Float> {
    public WaterSalinitySensor() {
        super("Water-Salinity", "μS/cm");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.getWeatherCondition();
        float currentValue = getValue() != null ? getValue() : 500.0f; // Valore iniziale
        switch (condition) {
            case LIGHT_RAIN -> currentValue -= (float) (Math.random() * 20); // Diluizione lieve
            case MODERATE_RAIN -> currentValue -= (float) (Math.random() * 50); // Diluizione moderata
            case HEAVY_RAIN, HURRICANE -> currentValue -= (float) (Math.random() * 100); // Diluizione significativa
            case SUNNY -> currentValue += (float) (Math.random() * 10); // Aumento per evaporazione
        }
        currentValue = Math.clamp(currentValue, 0.0f, 2000.0f); // Limita tra 0 e 2000 μS/cm
        setValue(currentValue);
        return currentValue;
    }

    /**
     * Converte la conducibilità da μS/cm a dS/m.
     */
    public Float getMeasurementInDSPerMeter() {
        Float valueInMicroSiemens = getValue();
        return valueInMicroSiemens != null ? valueInMicroSiemens / 1000 : null;
    }
}
