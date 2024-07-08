package com.example.service;

import com.example.model.Payment;
import com.example.model.PaymentData;
import com.example.model.Transaction;
import com.google.gson.Gson;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class DataHelper {

    protected static double finalTotals;
    protected static double footerGrossDeposits;
    protected static double footerInterChange;
    protected static double footerAssessmentAquireFee;

    protected static double footerOtherFee;


    public PaymentData processPaymentData(String jsonData) {
        Gson gson = new Gson();
        PaymentData paymentData = gson.fromJson(jsonData, PaymentData.class);
        return calculateTotals(paymentData);
    }

    public PaymentData calculateTotals(PaymentData paymentData) {
        for (Payment payment : paymentData.getPayments()) {
            double totalGrossDeposits = 0.0;
            double totalInterchange = 0.0;
            double totalAssessmentsAcquirerFee = 0.0;
            double netDeposit;

            for (Transaction transaction : payment.getTransactions()) {
                totalGrossDeposits += transaction.getGrossDeposits();
                totalInterchange += transaction.getInterchange();
                totalAssessmentsAcquirerFee += transaction.getAssessmentsAcquirerFee();
            }

            netDeposit = totalGrossDeposits - (totalInterchange + totalAssessmentsAcquirerFee);

            finalTotals += netDeposit;
            footerGrossDeposits += totalGrossDeposits;
            footerInterChange += totalInterchange;
            footerAssessmentAquireFee += totalAssessmentsAcquirerFee;

            Transaction totalTransaction = new Transaction();
            totalTransaction.setType("DayTotal");
            totalTransaction.setGrossDeposits(totalGrossDeposits);
            totalTransaction.setInterchange(totalInterchange);
            totalTransaction.setAssessmentsAcquirerFee(totalAssessmentsAcquirerFee);
            totalTransaction.setOtherFees(0.0);
            totalTransaction.setNetDeposit(netDeposit);

            payment.getTransactions().add(totalTransaction);
        }

        return paymentData;
    }


    protected Map<String, Map<String, Double>> calculateCardTotals(PaymentData paymentData, int pageSize) {
        // Initialize totals map for each card type
        Map<String, Map<String, Double>> cardTotalsMap = new HashMap<>();

        int totalPages = (int) Math.ceil((double) paymentData.getPayments().size() / pageSize);

        // Process each page
        for (int page = 0; page < totalPages; page++) {
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, paymentData.getPayments().size());

            for (int i = startIndex; i < endIndex; i++) {
                Payment payment = paymentData.getPayments().get(i);
                for (Transaction transaction : payment.getTransactions()) {
                    String cardType = transaction.getType();

                    Map<String, Double> typeTotals = cardTotalsMap.computeIfAbsent(cardType, k -> new HashMap<>());
                    typeTotals.put("Gross Deposits", typeTotals.getOrDefault("Gross Deposits", 0.0) + transaction.getGrossDeposits());
                    typeTotals.put("Inter Change", typeTotals.getOrDefault("Inter Change", 0.0) + transaction.getInterchange());
                    typeTotals.put("Assessments & Acquirer Fee", typeTotals.getOrDefault("Assessments & Acquirer Fee", 0.0) + transaction.getAssessmentsAcquirerFee());
                }
            }
        }

        return cardTotalsMap;
    }


    protected Table createTotalsTable(Map<String, Map<String, Double>> cardTotalsMap) {
        Table table = new Table(4);
        table.addCell(createHeaderCell("Card Type"));
        table.addCell(createHeaderCell("Gross Deposits"));
        table.addCell(createHeaderCell("Inter Change"));
        table.addCell(createHeaderCell("Assessments & Acquirer Fee"));

        for (Map.Entry<String, Map<String, Double>> entry : cardTotalsMap.entrySet()) {
            String cardType = entry.getKey();
            Map<String, Double> typeTotals = entry.getValue();

            table.addCell(createCell(cardType));
            table.addCell(createCell(typeTotals.getOrDefault("Gross Deposits", 0.0).toString()));
            table.addCell(createCell(typeTotals.getOrDefault("Inter Change", 0.0).toString()));
            table.addCell(createCell(typeTotals.getOrDefault("Assessments & Acquirer Fee", 0.0).toString()));
        }

        return table;
    }

    private Cell createHeaderCell(String header) {
        Cell cell = new Cell();
        cell.add(new Paragraph(header));
//        cell.setBackgroundColor(DeviceGray.GRAY);
//        cell.setTextColor(Color.WHITE);
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }

    private Cell createCell(String content) {
        Cell cell = new Cell();
        cell.add(new Paragraph(content));
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }


    //this method will return the number of cards the user entered in json.
    protected int numberOfCards(PaymentData paymentData) {

        return cardTypes(paymentData).size() - 1;
    }

    protected Set cardTypes(PaymentData paymentData){
        Set numberOfCardTypes = new HashSet();
        for (Payment payment : paymentData.getPayments()) {
            for (Transaction transaction : payment.getTransactions()) {
                numberOfCardTypes.add(transaction.getType());
            }
        }
        return numberOfCardTypes;
    }

    protected int getTotalNumberOfPages(int size,PaymentData paymentData){

        int totalRecords=0;

        for (Payment payment:paymentData.getPayments()){
            for (Transaction transaction:payment.getTransactions()){
                totalRecords++;
            }
        }
        return (totalRecords/size)+1;
    }
}
