package com.sapienter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.sapienter.hbaseClient.ui.MainNavigator;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * 
 */
@Theme("mytheme")
@PreserveOnRefresh
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        SpringContext.init();
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Cheese CMS");
        setNavigator(new Navigator(this, this));
        MainNavigator.init(getNavigator());
        getNavigator().navigateTo(MainNavigator.VAADIN_HOME);
    }


    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
        }
    }
}
