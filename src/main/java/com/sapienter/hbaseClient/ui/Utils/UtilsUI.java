package com.sapienter.hbaseClient.ui.Utils;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by Redorc on 15/02/2015.
 */
public class UtilsUI {

    private static boolean applyBorders = false;

    public static void addComponent(Layout container, Component contained) {
        if (applyBorders) container.addComponent(new Panel(contained));
        else container.addComponent(contained);
    }

    public static void addComponent(Layout container, Layout contained) {
        if (applyBorders) container.addComponent(new Panel(contained));
        else container.addComponent(contained);
    }

    public static void addOnRightSide(VerticalLayout verticalLayout, Label label) {
        verticalLayout.addComponent(label);
        verticalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
    }

    public static Component htmlLabel(String content) {
        return new Label(content, ContentMode.HTML);
    }

    public static Component htmlLabel(String content, String containerElement) {
        String containedString = "<" + containerElement + ">" +
                content + "<" + containerElement + "/>";
        return new Label(containedString, ContentMode.HTML);
    }

    public static void addToCenter(AbstractOrderedLayout layout, Component component) {
        layout.addComponent(component);
        component.setWidth(600, Sizeable.Unit.PIXELS);
        layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
        layout.setWidth("100%");
    }
}
