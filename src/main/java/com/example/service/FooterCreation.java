package com.example.service;

import com.example.model.PaymentData;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class FooterCreation {

    @Autowired
    private DataHelper dataHelper;

     int pageNumber;

    protected Table footer(PaymentData paymentData, int rowSize) {
        Table footer = createFooterTable();

        addPageCountCell(footer,paymentData,rowSize);
        addTotalFooterCells(footer,paymentData);

        addFooterTextCell(footer);

        return footer.setMarginTop(10);
    }

    // Method to create the main footer table with column widths and alignment
    private Table createFooterTable() {
        float[] columnWidth = {150f, 150f};
        Table footerTable = new Table(UnitValue.createPercentArray(columnWidth)).setFontSize(6)
                .setWidth(UnitValue.createPercentValue(100)).setHeight(140);
        return footerTable;
    }

    // Method to add the page count cell to the footer table
    private void addPageCountCell(Table footerTable, PaymentData paymentData, int rowSize) {
        float[] pageColumnWidth = {150f};
        Table pageCountTable = new Table(UnitValue.createPercentArray(pageColumnWidth))
                .setWidth(260).setHeight(100).setHorizontalAlignment(HorizontalAlignment.LEFT);
        int totalPages = dataHelper.getTotalNumberOfPages(rowSize,paymentData);
        pageCountTable.addCell(new Cell().add(new Paragraph("Page "+(++pageNumber)+" of "+totalPages)
                        .setFontColor(ColorConstants.RED))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        footerTable.addCell(new Cell().add(pageCountTable).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(Border.NO_BORDER));
    }

    // Method to add the total footer cells to the footer table
    private void addTotalFooterCells(Table footerTable,PaymentData paymentData) {
        float[] totalFooterColumnWidth = {150f, 60f};
        int totalPage = dataHelper.getTotalNumberOfPages(34,paymentData);

        Table totalFooterTable = new Table(UnitValue.createPercentArray(totalFooterColumnWidth))
                .setWidth(270).setHeight(100).setHorizontalAlignment(HorizontalAlignment.RIGHT).setBorder(Border.NO_BORDER);

        addRow(totalFooterTable, "Gross Deposits", String.valueOf(DataHelper.footerGrossDeposits));
        addRow(totalFooterTable, "Less: Interchange", String.valueOf(DataHelper.footerInterChange));
        addRow(totalFooterTable, "Less: Assessment & Acquirer Fee", String.valueOf(DataHelper.footerAssessmentAquireFee));
        addRow(totalFooterTable, "Other Fees", String.valueOf(DataHelper.footerOtherFee));
        addRow(totalFooterTable, "Net Deposit", String.valueOf(DataHelper.finalTotals));
        addRow(totalFooterTable, "", ""); // Empty row
        addRow(totalFooterTable, "Effective All-In Rate", "2.04");

        footerTable.addCell(new Cell().add(totalFooterTable).setHorizontalAlignment(HorizontalAlignment.RIGHT).setBorder(Border.NO_BORDER));
    }

    // Method to add the footer text cell to the footer table
    private void addFooterTextCell(Table footerTable) {
        Cell footerTextCell = new Cell(1, 2)
                .add(new Paragraph("Boost Payment Solutions,Inc | 767 Third Avenue | New York, NY 10017 | T (888)222-7122 | F (646)219-6100 | www.boostb2b.com")
                        .setUnderline().setFontColor(new DeviceRgb(Color.BLUE)))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER);

        footerTable.addCell(footerTextCell);
    }

    // Method to add a row with key-value pair to a table
    private void addRow(Table table, String key, String value) {
        table.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(10)
                        .add(new Paragraph(key))
                        .setBackgroundColor(new DeviceRgb(128, 128, 128), 0.5f))
                .setBold()
                .addCell(new Cell()
                        .add(new Paragraph(value)).setHeight(8)
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
    }
}
