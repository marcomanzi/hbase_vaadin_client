package com.sapienter.hbaseClient.ui.vaadin;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Created by marcolin on 09/03/15.
 */
public class FilterComponent extends AbsoluteLayout {

    public static final int HEIGHT = 60;
    private String filterTitle;
    private String filterCaption;
    private TextField filterValue;

    public FilterComponent(String filterTitle, String filterCaption) {
        this.filterCaption = filterCaption;
        this.filterTitle = filterTitle;
        filterValue = new TextField();

        setWidth("150px");
        setHeight(HEIGHT + "px");
        addComponent(new Label(filterTitle), "left:5px;top:2px;");
        addComponent(new Label(filterCaption), "left:5px;top:27px;");
        addComponent(filterValue, "right:5px;top:25px;");
        filterValue.setWidth("100px");
    }

}
