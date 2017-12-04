package dev.agrogps.server.webapp;

import dev.agrogps.server.service.ConfigServiceJAXRS;
import dev.agrogps.server.service.SensorsServiceJAXRS;
import dev.agrogps.server.service.TracesServiceJAXRS;
import dev.agrogps.server.service.TrackingServiceJAXRS;
import org.glassfish.jersey.server.ResourceConfig;


public class JerseyApplication extends ResourceConfig {

    public JerseyApplication() {

        this.register(ConfigServiceJAXRS.class);
        this.register(SensorsServiceJAXRS.class);
        this.register(TracesServiceJAXRS.class);
        this.register(TrackingServiceJAXRS.class);

    }
}
