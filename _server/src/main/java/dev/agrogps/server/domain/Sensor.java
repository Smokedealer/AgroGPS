package dev.agrogps.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "sensors")
public class Sensor extends BaseObject {

    private double x;
    private double y;
    private double radius;
    private String name;


    public Sensor() {
    }

    public Sensor(double x, double y, double radius, String name) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.name = name;
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

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String time) {
        this.name = time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Double.compare(sensor.x, this.x) == 0 &&
                Double.compare(sensor.y, this.y) == 0 &&
                Double.compare(sensor.radius, this.radius) == 0 &&
                Objects.equals(this.name, sensor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.radius, this.name);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", name='" + name + '\'' +
                '}';
    }
}
