package com.sapienter.hbaseClient.ui;

import com.sapienter.hbaseClient.ui.vaadin.VaadinHome;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redorc on 23/02/2015.
 */
public class MainNavigator implements ViewChangeListener {

    public final static String VAADIN_HOME = "vaadin_home";
    public static String ORDER_BUILDER = "order_builder";
    public final static List<String> securePages = new ArrayList<String>() {{

    }};
    private static Navigator navigator;

    private MainNavigator() {}

    public static void init(Navigator navigator) {
        MainNavigator.navigator = navigator;
        navigator.addView(VAADIN_HOME, new VaadinHome());
        navigator.addViewChangeListener(new MainNavigator());
    }

    public static void navigateTo(String view) {
        UI.getCurrent().getNavigator().navigateTo(view);
    }

    @Override
    public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
        return !(securePages.contains(viewChangeEvent.getViewName()));
    }

    @Override
    public void afterViewChange(ViewChangeEvent viewChangeEvent) {}
}
