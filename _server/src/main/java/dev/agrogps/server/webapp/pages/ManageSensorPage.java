package dev.agrogps.server.webapp.pages;

import dev.agrogps.server.domain.Sensor;
import dev.agrogps.server.manager.SensorManager;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.Collections;

public class ManageSensorPage extends WebPage {

    @SpringBean
    transient private SensorManager sensorsManager;

    private class FormData implements Serializable {
        public String x;
        public String y;
        public String radius;
        public String name;
    }

    private FormData formData = new FormData();

    public ManageSensorPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new BookmarkablePageLink("linkHome", HomePage.class));


        Form<?> form = new Form("form"){
            @Override
            protected void onSubmit() {
                super.onSubmit();

                FormData data = ManageSensorPage.this.formData;
                if(data.x == null) return;
                if(data.y == null) return;
                if(data.radius == null) return;
                if(data.name == null) return;

                try {
                    ManageSensorPage.this.sensorsManager.addSensors(Collections.singletonList(
                            new Sensor(
                                    Double.parseDouble(data.x),
                                    Double.parseDouble(data.y),
                                    Double.parseDouble(data.radius),
                                    data.name
                            )
                    ));
                    ManageSensorPage.this.error("Čidlo bylo přidáno");
                }
                catch (Exception ignored) {
                    ManageSensorPage.this.error("Chyba při přidávání");
                }

            }
        };
        form.add(new TextField<>("x", new PropertyModel<>(this.formData, "x")));
        form.add(new TextField<>("y", new PropertyModel<>(this.formData, "y")));
        form.add(new TextField<>("radius", new PropertyModel<>(this.formData, "radius")));
        form.add(new TextField<>("name", new PropertyModel<>(this.formData, "name")));

        this.add(form);


        this.add(new ComponentFeedbackPanel("feedback", this));

    }
}
