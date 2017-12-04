package dev.agrogps.server.manager;

import dev.agrogps.server.domain.Position;
import dev.agrogps.server.domain.Trace;

import java.util.List;


public interface TrackingManager {
    List<Position> loadPositions();

    List<Position> loadPositions(Long trackingId) throws Exception;

    List<Trace> loadTraces();

    void addPositions(Long trackingId, List<Position> newPositions) throws Exception;

    Trace addTrace();

    Trace finishTrace(Long trackingId) throws Exception;

    void clearTraces();
}
