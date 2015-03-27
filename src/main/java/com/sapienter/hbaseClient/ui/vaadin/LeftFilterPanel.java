package com.sapienter.hbaseClient.ui.vaadin;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by marcolin on 13/03/15.
 */
public class LeftFilterPanel extends Panel {

    public LeftFilterPanel() {
        super("Filters");
        setWidth(160, Unit.PIXELS);
        this.setHeight((getFilters().getComponentCount() * FilterComponent.HEIGHT + 35) + "px");
        this.setContent(getFilters());
    }

    private VerticalLayout getFilters() {
        VerticalLayout filters = new VerticalLayout();
        filters.addComponent(new FilterComponent("ID", "ID"));
        filters.addComponent(new FilterComponent("PRODUCT CODE", "Code"));
        filters.addComponent(new FilterComponent("DESCRIPTION", ""));
        return filters;
    }

}
