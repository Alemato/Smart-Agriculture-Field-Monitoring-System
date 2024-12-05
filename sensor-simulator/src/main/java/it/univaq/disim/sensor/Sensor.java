package it.univaq.disim.sensor;

import it.univaq.disim.world.ClimateContext;

public interface Sensor<T> {
    String getName();

    void setName(String name);

    String getUnit();

    void setUnit(String unit);

    T getValue();

    void setValue(T value);

    T getMeasurement(ClimateContext context);
}
