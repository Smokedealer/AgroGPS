package dev.agrogps.server.dao;

import dev.agrogps.server.domain.Sensor;

import java.util.List;


public interface SensorDao extends GenericDao<Sensor> {

    List<Sensor> findAll();

}
