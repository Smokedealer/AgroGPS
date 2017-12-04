package dev.agrogps.server.manager;

import dev.agrogps.server.dao.PositionDao;
import dev.agrogps.server.dao.TraceDao;
import dev.agrogps.server.domain.Position;
import dev.agrogps.server.domain.Trace;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DefaultTrackingManager implements TrackingManager {

    private PositionDao positionDao;
    private TraceDao traceDao;

    @Autowired
    public DefaultTrackingManager(PositionDao positionDao, TraceDao traceDao) {
        this.positionDao = positionDao;
        this.traceDao = traceDao;
    }


    @Override
    public List<Position> loadPositions() {
        return this.positionDao.findAll();
    }

    @Override
    public List<Position> loadPositions(Long trackingId) throws Exception {
        List<Position> positions = this.findTrace(trackingId).getPositions();
        Hibernate.initialize(positions);
        return positions;
    }

    @Override
    public void addPositions(Long trackingId, List<Position> newPositions) throws Exception {
        Trace trace = this.findTrace(trackingId);
        if(trace.getEndTime() != 0) {
            throw new Exception("Trace is already finished");
        }

        for (Position position : newPositions) {
            position.setTrace(trace);
            this.positionDao.save(position);
        }
    }


    @Override
    public List<Trace> loadTraces() {
        return this.traceDao.findAll();
    }

    @Override
    public Trace addTrace() {
        return this.traceDao.save(new Trace());
    }

    @Override
    public Trace finishTrace(Long trackingId) throws Exception {
        Trace trace = this.findTrace(trackingId);
        if(trace.getEndTime() != 0) {
            throw new Exception("Trace is already finished");
        }

        trace.setEndTimeAsNow();
        return this.traceDao.save(trace);
    }

    @Override
    public void clearTraces() {
        List<Trace> traces = this.traceDao.findAll();
        for (Trace trace : traces) {
            this.traceDao.remove(trace);
        }
    }


    private Trace findTrace(Long trackingId) throws Exception {
        Trace trace = this.traceDao.findOne(trackingId);
        if(trace == null) {
            throw new Exception("Trace not found");
        }
        return trace;
    }

}
