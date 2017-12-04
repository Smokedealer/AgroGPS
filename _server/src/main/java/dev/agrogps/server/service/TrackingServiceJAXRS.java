package dev.agrogps.server.service;

import dev.agrogps.server.domain.Trace;
import dev.agrogps.server.dto.TrackingDTO;
import dev.agrogps.server.manager.TrackingManager;
import dev.agrogps.server.utils.Responses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/tracking")
@Produces(MediaType.APPLICATION_JSON)
public class TrackingServiceJAXRS {

    private static final Logger logger = LoggerFactory.getLogger(TrackingServiceJAXRS.class);

    @Context
    private UriInfo uriInfo;

    private TrackingManager trackingManager;

    @Autowired
    public TrackingServiceJAXRS(TrackingManager trackingManager) {
        this.trackingManager = trackingManager;
    }




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPositions(TrackingDTO trackingDTO) {

        try {

            switch (trackingDTO.getAction()) {
                case "new": {
                    Trace t = this.trackingManager.addTrace();
                    return Responses.singleValue("trackingId", t.getId());
                }

                case "finish": {
                    this.trackingManager.finishTrace(trackingDTO.getTrackingId());
                    return Responses.success();
                }

                case "push": {
                    this.trackingManager.addPositions(trackingDTO.getTrackingId(), trackingDTO.getData());
                    return Responses.success();
                }
            }

            throw new Exception("Unknown action");
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return Responses.error(400, ex.getMessage());
        }

    }
}
