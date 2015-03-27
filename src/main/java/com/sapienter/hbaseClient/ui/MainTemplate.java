package com.sapienter.hbaseClient.ui;

import com.sapienter.hbaseClient.ui.Utils.UtilsUI;
import com.sapienter.hbaseClient.ui.fixed.Banner;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by Redorc on 15/02/2015.
 */
public abstract class MainTemplate extends VerticalLayout implements View {

    private Component centerComponent;

    public MainTemplate() {
        setSpacing(true);
        UtilsUI.addComponent(this, new Banner());
        refreshCenterComponent();
    }

    protected abstract Component getCenterComponent();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        refreshCenterComponent();
    }

    public void refreshCenterComponent() {
        if (centerComponent != null) this.removeComponent(centerComponent);
        centerComponent = getCenterComponent();
        UtilsUI.addComponent(this, centerComponent);
    }

}