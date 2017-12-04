package dev.agrogps.server.webapp.pages;

import dev.agrogps.server.domain.Trace;
import dev.agrogps.server.manager.TrackingManager;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public class TrackingPage extends WebPage {

    @SpringBean
    transient private TrackingManager trackingManager;


    public TrackingPage() {
        super();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();


        this.add(new DataView<Trace>("traces", new ListDataProvider<>(this.trackingManager.loadTraces())) {
            private static final long serialVersionUID = 1L;

            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(Locale.getDefault())
                            .withZone(ZoneId.systemDefault());

            @Override
            protected void populateItem(final Item<Trace> item)
            {
                Trace trace = item.getModelObject();
                String start = this.formatter.format(Instant.ofEpochSecond(trace.getStartTime()));
                String end = "";
                if(trace.getEndTime() != 0) end = this.formatter.format(Instant.ofEpochSecond(trace.getEndTime()));


                item.add(new Label("id", String.valueOf(trace.getId())));
                item.add(new Label("starttime", start));
                item.add(new Label("endtime", end));
                item.add(new BookmarkablePageLink("link", TracePage.class, new PageParameters(){{ this.add("id", trace.getId()); }}));

                item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public String getObject()
                    {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        });


        //this.add(new ComponentFeedbackPanel("feedback", this));

    }


}
