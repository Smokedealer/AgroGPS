package dev.agrogps.server.dao;

import dev.agrogps.server.domain.Position;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;


@Repository
public class PositionDaoJpa extends GenericDaoJpa<Position> implements PositionDao {

    public PositionDaoJpa() {
        super(Position.class);
    }

    @Override
    public List<Position> findAll() {
        TypedQuery<Position> q = this.em.createQuery("SELECT p FROM Position p ORDER BY p.trace.id, p.time", Position.class);

        try {
            return q.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }


}
