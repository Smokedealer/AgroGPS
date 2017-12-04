package dev.agrogps.server.dao;

import dev.agrogps.server.domain.Trace;

import java.util.List;


public interface TraceDao extends GenericDao<Trace> {


    List<Trace> findAll();
}
