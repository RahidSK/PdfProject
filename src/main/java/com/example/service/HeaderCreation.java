package com.example.service;

import com.example.dummyData.DummyData;
import com.example.model.BusinessEntity;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HeaderCreation {

    // Header Method
    protected Table header() throws IOException {
        float[] headColumnWidth = {150f, 150f};
        Table headerTable = new Table(UnitValue.createPointArray(headColumnWidth)).setWidth(UnitValue.createPercentValue(100))
                .setHeight(130);

        addLogoCell(headerTable);
        addTitleCell(headerTable);

        Table dataTable = createDataTable();
        Table queryTable = createQueryTable();

        headerTable.addCell(new Cell().add(dataTable).setBorder(Border.NO_BORDER))
                .addCell(new Cell().add(queryTable).setBorder(Border.NO_BORDER))
                .setMarginBottom(10);

        return headerTable;
    }

    // Method to add logo cell to the header table
    private void addLogoCell(Table headerTable) throws IOException {
        Image logo = new Image(ImageDataFactory.create("src/main/resources/templates/logo.jpg"))
                .setWidth(40).setHeight(30);
        headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
    }

    // Method to add title cell to the header table
    private void addTitleCell(Table headerTable) {
        headerTable.addCell(new Cell().add(new Paragraph("Monthly Billing Report")
                        .setTextAlignment(TextAlignment.CENTER)
                                .setVerticalAlignment(VerticalAlignment.TOP)
                        .setBold())
                .setBorder(Border.NO_BORDER));
    }

    // Method to create data table with business entity information
    private Table createDataTable() {
        float[] dataColumnWidth = {60f, 150f};
        Table dataTable = new Table(UnitValue.createPointArray(dataColumnWidth)).setFontSize(6)
                .setWidth(260).setHeight(100).setHorizontalAlignment(HorizontalAlignment.LEFT);
        List<BusinessEntity> entityDataList = DummyData.setValues();

        for (BusinessEntity entityData : entityDataList) {
            addRow(dataTable, "Date:", entityData.getDate());
            addRow(dataTable, "Summary Period:", entityData.getSummaryPeriod());
            addRow(dataTable, "Merchant Number:", String.valueOf(entityData.getMerchantNumber()));
            addRow(dataTable, "Business Name:", entityData.getBusinessName());
            addRow(dataTable, "Mailing Address:", entityData.getMailingAddress());
            addRow(dataTable, "Sub MID Number:", String.valueOf(entityData.getSubMIDNumber()));
        }

        return dataTable;
    }

    // Method to create query table with contact information
    private Table createQueryTable() {
        float[] queryTableWidth = {150f};
        Table queryTable = new Table(UnitValue.createPointArray(queryTableWidth))
                .setWidth(260)
                .setFontSize(6)
                .setHeight(100)
                .setFontColor(ColorConstants.RED)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT);

        queryTable.addCell(new Cell()
                .add(new Paragraph("If you have any questions regarding your monthly summary\n " +
                        "please contact our Customer Service Team at (888) 222-7122.\n Thank You."))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBold()
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

        return queryTable;
    }

    // Method to add a row with key-value pair to a table
    private void addRow(Table table, String key, String value) {
        table.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .add(new Paragraph(key))
                        .setBackgroundColor(new DeviceRgb(128, 128, 128), 0.5f))
                .setBold()
                .addCell(new Cell()
                        .add(new Paragraph(value))
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
    }
}
