package dev.agrogps.server.dao;

import dev.agrogps.server.domain.Sensor;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;


@Repository
public class SensorDaoJpa extends GenericDaoJpa<Sensor> implements SensorDao {

    public SensorDaoJpa() {
        super(Sensor.class);
    }

    @Override
    public List<Sensor> findAll() {
        TypedQuery<Sensor> q = this.em.createQuery("SELECT s FROM Sensor s", Sensor.class);

        try {
            return q.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}
