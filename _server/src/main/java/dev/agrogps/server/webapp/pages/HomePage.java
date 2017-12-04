package dev.agrogps.server.webapp.pages;

import dev.agrogps.server.domain.Trace;
import dev.agrogps.server.manager.SensorManager;
import dev.agrogps.server.manager.TrackingManager;
import dev.agrogps.server.utils.TestData;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage extends WebPage {

    @SpringBean
    transient private TrackingManager trackingManager;

    @SpringBean
    transient private SensorManager sensorsManager;


    private String relativePathTo(String subpath) {
        return RequestUtils.toAbsolutePath(RequestCycle.get().getRequest().getPrefixToContextPath(), subpath);
    }

    public HomePage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new BookmarkablePageLink("linkTracking", TrackingPage.class));
        this.add(new BookmarkablePageLink("linkManageSensor", ManageSensorPage.class));
        this.add(new ExternalLink("linkAPITraces", this.relativePathTo("api/trace")));
        this.add(new ExternalLink("linkAPISensors", this.relativePathTo("api/sensors")));
        this.add(new ExternalLink("linkAPIConfig", this.relativePathTo("api/config")));


        this.add(new Link("linkDBReset"){
            @Override
            public void onClick()
            {
                HomePage.this.trackingManager.clearTraces();
                HomePage.this.sensorsManager.clearSensors();
                this.info("Databáze byla vyčištěna");
            }
        });

        this.add(new Link("linkDBInitSensors"){
            @Override
            public void onClick()
            {
                HomePage.this.sensorsManager.addSensors(TestData.sensors);
                this.info("Byla přidána 2 čidla");
            }
        });

        this.add(new Link("linkDBInitTestData"){
            @Override
            public void onClick()
            {
                try {
                    Trace t = HomePage.this.trackingManager.addTrace();
                    HomePage.this.trackingManager.addPositions(t.getId(), TestData.positions);
                    HomePage.this.trackingManager.finishTrace(t.getId());
                    this.info("Byla přidána testovací data");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        this.add(new ComponentFeedbackPanel("feedback", this));
    }
}
