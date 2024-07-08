package com.example.service;

import com.example.model.Transaction;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

@Service
public class TableCreations {

    public Table createMainTable() {
        float[] columnWidth = {75f, 25f, 85f, 85f, 75f, 75f, 75f};
        Table mainTable = new Table(UnitValue.createPointArray(columnWidth)).setFontSize(6).setWidth(UnitValue.createPercentValue(100));

        addHeaderRow(mainTable);
        addColumnHeaders(mainTable);

        return mainTable;
    }

    private void addHeaderRow(Table mainTable) {
        mainTable.addCell(new Cell(1, 7)
                .add(new Paragraph("MONTHLY DEPOSIT SUMMARY").setTextAlignment(TextAlignment.CENTER))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8)
                .setBackgroundColor(new DeviceRgb(128, 128, 128), 0.5f));
    }

    private void addColumnHeaders(Table mainTable) {
        mainTable.addCell(createHeaderCell("Payment Funding Date"));
        mainTable.addCell(createHeaderCell("Type"));
        mainTable.addCell(createHeaderCell("Gross Deposits"));
        mainTable.addCell(createHeaderCell("Inter Change"));
        mainTable.addCell(createHeaderCell("Assessments & Acquirer Fee"));
        mainTable.addCell(createHeaderCell("Other Fee"));
        mainTable.addCell(createHeaderCell("Net Deposit"));
    }

    private Cell createHeaderCell(String headerText) {
        return new Cell().add(new Paragraph(headerText)).setBorder(new SolidBorder(ColorConstants.BLACK, 1));
    }

    public void addTransactionRow(Table mainTable, String date, Transaction transaction) {
        if (transaction.getType().equals("DayTotal")) {
            addEmptyCells(mainTable, 2);
        } else {
            mainTable.addCell(new Cell().add(new Paragraph(date)).setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
            mainTable.addCell(new Cell().add(new Paragraph(transaction.getType())).setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
        }

        mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getGrossDeposits()))).setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
        mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getInterchange()))).setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
        mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getAssessmentsAcquirerFee()))).setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));

        if (transaction.getOtherFees() == 0) {
            addEmptyCell(mainTable);
        } else {
            mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getOtherFees())).setBorder(new SolidBorder(ColorConstants.BLACK, 1))).setHeight(8));
        }

        mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getNetDeposit()))).setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
    }

    private void addEmptyCells(Table mainTable, int numCells) {
        for (int i = 0; i < numCells; i++) {
            mainTable.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
        }
    }

    private void addEmptyCell(Table mainTable) {
        mainTable.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
    }

//    public void addFinalTotalsRow(Table mainTable, double totalOtherFees, double netTotal) {
//        mainTable.addCell(new Cell(1, 5)
//                .add(new Paragraph("TOTALS"))
//                .setBackgroundColor(new DeviceRgb(128, 128, 128), 0.5f).setHeight(8));
//
//        mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalOtherFees))).setHeight(8));
//        mainTable.addCell(new Cell().add(new Paragraph(String.valueOf(netTotal))).setHeight(8));
//    }
}
