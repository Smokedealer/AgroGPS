package dev.agrogps.server.dao;

import dev.agrogps.server.domain.Position;

import java.util.List;



public interface PositionDao extends GenericDao<Position> {

    List<Position> findAll();

}
