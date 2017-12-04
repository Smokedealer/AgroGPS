package dev.agrogps.server.service;

import dev.agrogps.server.utils.Responses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigServiceJAXRS {

    @GET
    public Response loadPostsWithComments() {
        Map<String, Object> config = new HashMap<>();

        config.put("interval_tracking", 5);
        config.put("interval_server_push", 30);

        return Responses.singleValue("data", config);
    }

}
