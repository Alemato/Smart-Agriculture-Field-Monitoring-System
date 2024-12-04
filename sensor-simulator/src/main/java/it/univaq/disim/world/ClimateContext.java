package it.univaq.disim.world;

public class ClimateContext {
    private boolean isRaining;
    private float externalTemperature; // Temperatura esterna
    private float relativeHumidity;    // Umidità relativa

    public ClimateContext(boolean isRaining, float externalTemperature, float relativeHumidity) {
        this.isRaining = isRaining;
        this.externalTemperature = externalTemperature;
        this.relativeHumidity = relativeHumidity;
    }

    public boolean isRaining() {
        return isRaining;
    }

    public void setRaining(boolean raining) {
        isRaining = raining;
    }

    public float getExternalTemperature() {
        return externalTemperature;
    }

    public void setExternalTemperature(float externalTemperature) {
        this.externalTemperature = externalTemperature;
    }

    public float getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(float relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }
}
