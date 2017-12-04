package dev.agrogps.server.dao;

import dev.agrogps.server.domain.BaseObject;


public interface GenericDao<T extends BaseObject> {

    T save(T value);

    T findOne(Long id);

    void remove(T toRemove);

}
