package dev.agrogps.server.service;

import dev.agrogps.server.manager.SensorManager;
import dev.agrogps.server.utils.Responses;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorsServiceJAXRS {

    private SensorManager sensorManager;

    @Autowired
    public SensorsServiceJAXRS(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    @GET
    public Response loadPostsWithComments() {
        return Responses.singleValue("data", this.sensorManager.loadSensors());
    }


}
