package dev.agrogps.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.agrogps.server.domain.Position;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingDTO {

    private String action;
    private Long trackingId;
    private List<Position> data;


    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getTrackingId() {
        return this.trackingId;
    }

    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }

    public List<Position> getData() {
        return this.data;
    }

    public void setData(List<Position> data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "TrackingDTO{" +
                "action='" + this.action + '\'' +
                ", trackingId=" + this.trackingId +
                ", data=" + this.data +
                '}';
    }
}
