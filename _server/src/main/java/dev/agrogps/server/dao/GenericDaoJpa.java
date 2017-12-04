package dev.agrogps.server.dao;

import dev.agrogps.server.domain.BaseObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class GenericDaoJpa<T extends BaseObject> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager em;

    private Class<T> persistedType;

    /**
     * @param persistedType type of the entity persisted by this DAO
     */
    public GenericDaoJpa(Class<T> persistedType) {
        this.persistedType = persistedType;
    }

    @Override
    public T save(T value) {
        if(value.isNew()) {
            this.em.persist(value);
            return value;
        } else {
            return this.em.merge(value);
        }
    }

    @Override
    public T findOne(Long id) {
        return this.em.find(this.persistedType, id);
    }

    @Override
    public void remove(T toRemove) {
        if(!toRemove.isNew()) {
            this.em.remove(toRemove);
        }
    }


}
