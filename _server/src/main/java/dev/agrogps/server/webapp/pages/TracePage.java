package dev.agrogps.server.webapp.pages;

import dev.agrogps.server.domain.Position;
import dev.agrogps.server.manager.TrackingManager;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.Collections;
import java.util.List;


public class TracePage extends WebPage {

    @SpringBean
    private TrackingManager trackingManager;
    private PageParameters parameters;


    public TracePage(final PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        List<Position> positions = Collections.emptyList();
        try {
            StringValue parTrackingId = this.parameters.get("id");
            positions = this.trackingManager.loadPositions(parTrackingId.toLong());
        } catch (Exception ignored) {
        }

        this.add(new DataView<Position>("positions", new ListDataProvider<>(positions)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<Position> item)
            {
                Position pos = item.getModelObject();

                item.add(new Label("x", Double.toString(pos.getX()).replace(",",".")).setRenderBodyOnly(true));
                item.add(new Label("y", Double.toString(pos.getY()).replace(",",".")).setRenderBodyOnly(true));

                item.setRenderBodyOnly(true);
            }
        });


    }


}
