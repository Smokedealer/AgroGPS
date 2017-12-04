package dev.agrogps.server.dao;

import dev.agrogps.server.domain.Trace;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;


@Repository
public class TraceDaoJpa extends GenericDaoJpa<Trace> implements TraceDao {

    public TraceDaoJpa() {
        super(Trace.class);
    }

    @Override
    public List<Trace> findAll() {
        TypedQuery<Trace> q = this.em.createQuery("SELECT t FROM Trace t ORDER BY t.id", Trace.class);

        try {
            return q.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}
