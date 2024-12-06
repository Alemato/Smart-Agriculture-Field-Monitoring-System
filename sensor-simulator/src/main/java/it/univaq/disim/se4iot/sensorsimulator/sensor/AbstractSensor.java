package it.univaq.disim.se4iot.sensorsimulator.sensor;

public abstract class AbstractSensor<T> implements Sensor<T> {
    private String name;
    private String unit;
    private T value;

    protected AbstractSensor(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
