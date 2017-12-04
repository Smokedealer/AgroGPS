package dev.agrogps.server.service;

import dev.agrogps.server.domain.Trace;
import dev.agrogps.server.manager.TrackingManager;
import dev.agrogps.server.utils.Responses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@Path("/trace")
@Produces(MediaType.APPLICATION_JSON)
public class TracesServiceJAXRS {

    private static final Logger logger = LoggerFactory.getLogger(TracesServiceJAXRS.class);

    @Context
    private UriInfo uriInfo;

    private TrackingManager trackingManager;

    @Autowired
    public TracesServiceJAXRS(TrackingManager trackingManager) {
        this.trackingManager = trackingManager;
    }



    @GET
    public List<Trace> loadTraces() {
        return this.trackingManager.loadTraces();
    }

    @GET
    @Path("/{id}")
    public Response loadTrace(@PathParam("id") Long id) {
        try {
            return Responses.singleValue("data", this.trackingManager.loadPositions(id));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return Responses.error(400, ex.getMessage());
        }
    }

}
