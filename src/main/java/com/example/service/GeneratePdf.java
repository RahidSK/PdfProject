package com.example.service;

import com.example.model.Payment;
import com.example.model.PaymentData;
import com.example.model.Transaction;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;


import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeneratePdf {

    @Autowired
    private DataHelper dataHelper;
    @Autowired
    private TableCreations tableCreations;
    @Autowired
    private HeaderCreation headerCreation;
    @Autowired
    private FooterCreation footerCreation;

    public ByteArrayOutputStream createPdf(String jsonData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4);
        document.setMargins(20, 25, 20, 25);

        int pageCount = 0;

        try {
            PaymentData paymentData = dataHelper.processPaymentData(jsonData);
            int numberOfCards = dataHelper.numberOfCards(paymentData);
            int rowsPerPage = 34 - numberOfCards * 2;
            int currentPageRows = 0;

            Table mainTable = tableCreations.createMainTable();
            document.add(headerCreation.header());

            Map<String, Map<String, Double>> cardTotals = new HashMap<>();

            for (Payment payment : paymentData.getPayments()) {
                for (Transaction transaction : payment.getTransactions()) {
                    if (currentPageRows >= rowsPerPage) {
                        for (int i = 0; i < numberOfCards; i++) {
                            for (int j = 1; j <= 7; j++) {
                                mainTable.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
                            }
                        }

                        // Add page totals
                        addPageTotal(mainTable, cardTotals);

                        document.add(mainTable);
                        document.add(footerCreation.footer(paymentData,rowsPerPage));

                        document.add(new AreaBreak()).add(headerCreation.header());
                        mainTable = tableCreations.createMainTable();
                        currentPageRows = 0;

                        cardTotals.clear();
                    }

                    tableCreations.addTransactionRow(mainTable, payment.getDate(), transaction);
                    currentPageRows++;

                    // Update totals
                    updateTotals(transaction, cardTotals);
                }
            }

            for (int i = 0; i < numberOfCards; i++) {
                for (int j = 1; j <= 7; j++) {
                    mainTable.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.BLACK, 1)).setHeight(8));
                }
            }

            // Add final page totals
            addPageTotal(mainTable, cardTotals);

            document.add(mainTable);
            document.add(footerCreation.footer(paymentData,rowsPerPage));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            footerCreation.pageNumber=0;
        }

        return baos;
    }

    private void updateTotals(Transaction transaction, Map<String, Map<String, Double>> cardTotals) {
        String type = transaction.getType();
        cardTotals.putIfAbsent(type, new HashMap<>());
        Map<String, Double> totals = cardTotals.get(type);

        totals.put("gross_deposits", totals.getOrDefault("gross_deposits", 0.0) + transaction.getGrossDeposits());
        totals.put("interchange", totals.getOrDefault("interchange", 0.0) + transaction.getInterchange());
        totals.put("assessments_acquirer_fee", totals.getOrDefault("assessments_acquirer_fee", 0.0) + transaction.getAssessmentsAcquirerFee());
        totals.put("other_fees", totals.getOrDefault("other_fees", 0.0) + transaction.getOtherFees());
    }

    private void addPageTotal(Table table, Map<String, Map<String, Double>> cardTotals) {
        double totalNetDeposits = 0.0;

        for (String cardType : cardTotals.keySet()) {
            Map<String, Double> totals = cardTotals.get(cardType);
            double netDeposits = totals.get("gross_deposits") - totals.get("interchange") - totals.get("assessments_acquirer_fee") - totals.get("other_fees");
            totalNetDeposits += netDeposits;
            if(cardType.equals("DayTotal")) continue;
            table.addCell(new Cell(1, 2).add(new Paragraph(cardType)).setBold());
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", totals.get("gross_deposits")))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", totals.get("interchange")))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", totals.get("assessments_acquirer_fee")))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", totals.get("other_fees")))));
            table.addCell(new Cell().add(new Paragraph("")));
        }

        // Add final row with total net deposits
        for(int i=0;i<6; i++)
            table.addCell(new Cell());
        table.addCell(new Cell().add(new Paragraph(String.format("%.2f", totalNetDeposits))).setBold());
    }
}
