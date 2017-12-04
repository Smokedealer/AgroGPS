package dev.agrogps.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "traces")
public class Trace extends BaseObject {

    private long startTime;
    private long endTime;
    private List<Position> positions = new ArrayList<>();

    public Trace() {
        this.startTime = Instant.now().getEpochSecond();
    }

    public Trace(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTimeAsNow() {
        this.endTime = Instant.now().getEpochSecond();
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, mappedBy = "trace")
    public List<Position> getPositions() {
        return this.positions;
    }

    public void setPositions(List<Position> comments) {
        this.positions = comments;
    }

    public void addPosition(Position position) {
        position.setTrace(this);
        this.positions.add(position);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trace trace = (Trace) o;
        return this.startTime == trace.startTime &&
                this.endTime == trace.endTime &&
                Objects.equals(this.positions, trace.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.startTime, this.endTime, this.positions);
    }
}
