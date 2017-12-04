package dev.agrogps.server.webapp;

import dev.agrogps.server.webapp.pages.HomePage;
import dev.agrogps.server.webapp.pages.ManageSensorPage;
import dev.agrogps.server.webapp.pages.TracePage;
import dev.agrogps.server.webapp.pages.TrackingPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class WicketApplication extends WebApplication {

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }


    @Override
    public void init() {
        super.init();


        //make Spring injection work
        this.getComponentInstantiationListeners().add(new SpringComponentInjector(this, this.getContext(), false));
        this.mountPages();
    }


    private void mountPages() {
        this.mountPage("/manage/sensor", ManageSensorPage.class);
        this.mountPage("/tracking", TrackingPage.class);
        this.mountPage("/tracking/trace", TracePage.class);
    }

    protected ApplicationContext getContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    }
}
