package dev.agrogps.server.core.posts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.agrogps.server.core.posts.domain.Position;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDTO implements Serializable {


    @JsonProperty(value = "x")
    private double x;

    @JsonProperty(value = "y")
    private double y;

    @JsonProperty(value = "time")
    private int time;

    public SensorDTO() {
    }



    public SensorDTO(Position position) {
        this.x = position.getX();
        this.y = position.getY();
        this.time = position.getTime();
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

}
