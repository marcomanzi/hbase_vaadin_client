package com.sapienter.hbaseClient.ui.fixed;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

/**
 * Created by Redorc on 15/02/2015.
 */
public class Banner extends HorizontalLayout {

    public Banner() {
        AbsoluteLayout bannerContainer = new AbsoluteLayout();
        bannerContainer.setHeight(180, Unit.PIXELS);
        bannerContainer.addComponent(logo(), "left:20px; top:20px;");
        bannerContainer.setWidth(100, Unit.PERCENTAGE);
        addComponent(bannerContainer);
        setComponentAlignment(bannerContainer, Alignment.MIDDLE_CENTER);
        setWidth("100%");
        setHeight(80, Unit.PIXELS);
    }

    private Image logo() {
        return createThemeImage("img/logo.jpg", "jBilling", 107, 60);
    }

    private Image createThemeImage(String path, String alternateText,
                                   int width, int height) {
        Image image = new Image("", new ThemeResource(path));
        image.setAlternateText(alternateText);
        image.setHeight(height, Unit.PIXELS);
        image.setWidth(width, Unit.PIXELS);
        return image;
    }
}
