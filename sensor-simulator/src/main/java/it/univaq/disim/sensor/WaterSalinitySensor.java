package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;

public class WaterSalinitySensor extends AbstractSensor<Float> {
    public WaterSalinitySensor() {
        super("Water-Salinity", "μS/cm");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        float currentValue = getValue() != null ? getValue() : 500.0f; // Valore iniziale medio in μS/cm

        if (context.isRaining()) {
            currentValue -= (float) (Math.random() * 50 + 20); // Riduzione durante la pioggia
        } else {
            currentValue += (float) (Math.random() * 10 + 5); // Aumento durante la siccità
        }

        // Limiti realistici per la conducibilità dell'acqua dolce (in μS/cm)
        currentValue = Math.clamp(currentValue, 0.0f, 2000.0f);
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
