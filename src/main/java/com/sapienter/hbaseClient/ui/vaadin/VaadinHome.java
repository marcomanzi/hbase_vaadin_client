package com.sapienter.hbaseClient.ui.vaadin;

import com.sapienter.hbaseClient.business.hbase.HBaseExampleRow;
import com.sapienter.hbaseClient.business.hbase.HBaseExamples;
import com.sapienter.hbaseClient.business.utils.CallableThatThrows;
import com.sapienter.hbaseClient.ui.MainTemplate;
import com.sapienter.hbaseClient.ui.Utils.UtilsUI;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

import java.util.Date;
import java.util.List;

/**
 * Created by marcolin on 09/03/15.
 */
public class VaadinHome extends MainTemplate {

    private Table rowTable;
    private TextField cachingField;

    @Override
    protected Component getCenterComponent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        UtilsUI.addToCenter(verticalLayout, tableWithRowsFromHBase());
        UtilsUI.addToCenter(verticalLayout, getButtons());
        return verticalLayout;
    }

    private Component tableWithRowsFromHBase() {
        rowTable = new Table();
        rowTable.setWidth(100, Unit.PERCENTAGE);
        rowTable.setHeight(500, Unit.PIXELS);
        BeanItemContainer<HBaseExampleRow> tableContainer = new BeanItemContainer<>(HBaseExampleRow.class);
        rowTable.setContainerDataSource(tableContainer);
        return rowTable;
    }

    public Component getButtons() {
        VerticalLayout buttonLayout = new VerticalLayout();

        addButtonsToHandleElementsInHBase(buttonLayout);

        addPaginationButtons(buttonLayout);

        buttonLayout.addComponent(paginationExamples());
        return buttonLayout;
    }

    private void addButtonsToHandleElementsInHBase(VerticalLayout buttonLayout) {
        HorizontalLayout firstLine = new HorizontalLayout();
        firstLine.setSpacing(true);
        TextField prefixForRow = new TextField("Prefix for rows created:", "row");
        firstLine.addComponent(prefixForRow);
        firstLine.addComponent(new Button("Add 100000 rows in HBase With Prefix", e -> {
            HBaseExamples.get(caching()).create100000ExampleRows(prefixForRow.getValue());
        }));
        firstLine.addComponent(new Button("Delete all example rows in HBase", e -> {
            HBaseExamples.get(caching()).deleteAllExampleRows();
        }));
        buttonLayout.addComponent(firstLine);
    }

    private int caching() {
        return Integer.parseInt(cachingField.getValue());
    }

    private void addPaginationButtons(VerticalLayout buttonLayout) {
        HorizontalLayout secondLine = new HorizontalLayout();
        secondLine.setSpacing(true);
        Button multithreadButton = new Button("Get All rows with multithread",
                e -> updateTable(() -> HBaseExamples.get(caching()).retrieveAllExampleRowsInMultiThreadingWithMoreColumnPagination()));
        multithreadButton.setEnabled(false);
        secondLine.addComponent(new Button("Get All rows from HBase",
                e -> updateTable(() -> {
                    multithreadButton.setEnabled(true);
                    return HBaseExamples.get(caching()).retrieveAllExampleRows();
                })));
        secondLine.addComponent(multithreadButton);

        buttonLayout.addComponent(secondLine);
    }

    private void updateTable(CallableThatThrows<List<HBaseExampleRow>> consumer) {
        try {
            rowTable.removeAllItems();
            Date startDate = new Date();
            List<HBaseExampleRow> rows = consumer.apply();
            rows.forEach(r -> rowTable.addItem(r));
            rowTable.setCaption("Number of rows in table: " + rows.size() +
                    ", retrieved in " + (new Date().getTime() - startDate.getTime()) + " ms");
        } catch (Exception e) {}
    }

    private Component paginationExamples() {
        FormLayout layout = new FormLayout();
        layout.setSpacing(true);
        cachingField = new TextField("Caching:", "10");
        layout.addComponent(cachingField);
        TextField prefixForRow = new TextField("Row Prefix:", "row");
        layout.addComponent(prefixForRow);
        TextField offset = new TextField("Offset:", "0");
        layout.addComponent(offset);
        TextField limit = new TextField("Limit:", "10");
        layout.addComponent(limit);
        layout.addComponent(new Button("Get Paginated records with Page Filter",
                e -> updateTable(() -> HBaseExamples.get(caching()).retrievePaginatedRowsWithPageFilter(
                                Integer.parseInt(limit.getValue()),
                                Integer.parseInt(offset.getValue()))
                )));
        layout.addComponent(new Button("Get Paginated records with Page Filter and Prefix Filter",
                e -> updateTable(() -> HBaseExamples.get(caching()).retrievePaginatedRowsWithPageFilter(
                                prefixForRow.getValue(),
                                Integer.parseInt(limit.getValue()),
                                Integer.parseInt(offset.getValue()))
                )));
        layout.addComponent(new Button("Get Paginated records with Counter on a column",
                e -> updateTable(() -> HBaseExamples.get(caching()).retrievePaginatedRowsWithCounterOnColumn(
                                Integer.parseInt(limit.getValue()),
                                Integer.parseInt(offset.getValue()))
                )));

        layout.addComponent(new Button("Get Paginated records with Column Pagination Filter - Not Working",
                e -> updateTable(() -> HBaseExamples.get(caching()).retrievePaginatedRowsWithColumnPaginationFilter(
                                Integer.parseInt(limit.getValue()),
                                Integer.parseInt(offset.getValue()))
                )));
        return layout;
    }
}
