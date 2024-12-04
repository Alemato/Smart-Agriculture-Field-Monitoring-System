package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;

public class SoilPHSensor extends AbstractSensor<Float> {
    public SoilPHSensor() {
        super("Soil-pH", "");
    }

    @Override
    public Float getMeasurement(ClimateContext context) {
        float currentValue = getValue() != null ? getValue() : 6.5f; // Valore iniziale medio di pH
        if (context.isRaining()) {
            if (Math.random() < 0.3) { // 30% di probabilità di pioggia acida
                currentValue -= 0.2f + (float) Math.random() * 0.3f; // Riduzione del pH
            } else {
                currentValue += 0.1f; // Stabilizzazione verso valori neutri
            }
        } else {
            currentValue += (float) (Math.random() * 0.1 - 0.05); // Piccole fluttuazioni senza pioggia
        }
        currentValue = Math.clamp(currentValue, 4.0f, 8.5f); // Limita il pH tra 4.0 e 8.5
        setValue(currentValue);
        return currentValue;
    }
}
