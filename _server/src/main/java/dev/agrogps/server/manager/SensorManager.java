package dev.agrogps.server.manager;

import dev.agrogps.server.domain.Sensor;

import java.util.List;


public interface SensorManager {
    void addSensors(List<Sensor> newSensors);

    List<Sensor> loadSensors();

    void clearSensors();
}
