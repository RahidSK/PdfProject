package com.example.model;


import java.util.List;

public class Payment {

    private String date;
    private List<Transaction> transactions;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> fundingCardTransactions) {
        this.transactions = fundingCardTransactions;
    }
}
