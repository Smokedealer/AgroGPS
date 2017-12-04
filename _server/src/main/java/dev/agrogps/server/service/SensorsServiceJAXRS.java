package dev.agrogps.server.service;

import dev.agrogps.server.domain.Sensor;
import dev.agrogps.server.manager.SensorManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorsServiceJAXRS {

    private SensorManager sensorManager;

    @Autowired
    public SensorsServiceJAXRS(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    @GET
    public List<Sensor> loadPostsWithComments() {
        return this.sensorManager.loadSensors();
    }


}
