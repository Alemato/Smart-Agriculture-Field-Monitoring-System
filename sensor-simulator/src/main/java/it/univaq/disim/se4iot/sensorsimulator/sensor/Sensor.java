package it.univaq.disim.se4iot.sensorsimulator.sensor;

import it.univaq.disim.se4iot.sensorsimulator.world.ClimateContext;

public interface Sensor<T> {
    String getName();

    void setName(String name);

    String getUnit();

    void setUnit(String unit);

    T getValue();

    void setValue(T value);

    T getMeasurement(ClimateContext context);
}
