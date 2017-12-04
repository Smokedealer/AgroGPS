package dev.agrogps.server.manager;

import dev.agrogps.server.dao.SensorDao;
import dev.agrogps.server.domain.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DefaultSensorManager implements SensorManager {
    private SensorDao sensorDao;

    @Autowired
    public DefaultSensorManager(SensorDao sensorDao) {
        this.sensorDao = sensorDao;
    }


    @Override
    public void addSensors(List<Sensor> newSensors) {
        for (Sensor sensor : newSensors) {
            this.sensorDao.save(sensor);
        }
    }

    @Override
    public List<Sensor> loadSensors() {
        return this.sensorDao.findAll();
    }

    @Override
    public void clearSensors() {
        List<Sensor> sensors = this.sensorDao.findAll();
        for (Sensor sensor : sensors) {
            this.sensorDao.remove(sensor);
        }
    }
}
