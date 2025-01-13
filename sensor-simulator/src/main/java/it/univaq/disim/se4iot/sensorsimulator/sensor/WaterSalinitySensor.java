package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;
import it.univaq.disim.se4iot.sensorsimulator.world.WeatherCondition;

public class WaterSalinitySensor extends AbstractSensor<Float> {
    public WaterSalinitySensor() {
        super("Water-Salinity", "μS/cm");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        WeatherCondition condition = context.weatherCondition();
        float currentValue = getValue() != null ? getValue() : 500.0f; // Valore iniziale
        float adjustment = switch (condition) {
            case LIGHT_RAIN -> (float) (Math.random() * 20) * -1; // Diluizione lieve
            case MODERATE_RAIN -> (float) (Math.random() * 50) * -1; // Diluizione moderata
            case HEAVY_RAIN, HURRICANE -> (float) (Math.random() * 100) * -1; // Diluizione significativa
            case SUNNY -> (float) (Math.random() * 10); // Aumento per evaporazione
            default -> 0;
        };
        // Genera un segno random (+1 o -1)
        float sign = (Math.random() < 0.5) ? 1f : -1f;
        currentValue = currentValue + (sign * adjustment);
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
