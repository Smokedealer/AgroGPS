package dev.agrogps.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "positions")
public class Position extends BaseObject {


    private double x;
    private double y;
    private int time;
    private Trace trace;


    public Position() {
    }

    public Position(double x, double y, int time) {
        this.x = x;
        this.y = y;
        this.time = time;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Trace getTrace() {
        return this.trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    @Transient
    @JsonProperty("traceId")
    public Long getTraceId() {
        return this.trace.getId();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Position position = (Position) o;
        return Double.compare(position.x, this.x) == 0 &&
                Double.compare(position.y, this.y) == 0 &&
                this.time == position.time &&
                Objects.equals(this.trace, position.trace);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), this.x, this.y, this.time, this.trace);
    }
}
