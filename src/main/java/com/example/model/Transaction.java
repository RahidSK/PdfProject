package com.example.model;


public class Transaction {
    private String type;
    private double gross_deposits;
    private double interchange;
    private double assessments_acquirer_fee;
    private double other_fees;
    private double net_deposit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getGrossDeposits() {
        return gross_deposits;
    }

    public void setGrossDeposits(double gross_deposits) {
        this.gross_deposits = gross_deposits;
    }

    public double getInterchange() {
        return interchange;
    }

    public void setInterchange(double interchange) {
        this.interchange = interchange;
    }

    public double getAssessmentsAcquirerFee() {
        return assessments_acquirer_fee;
    }

    public void setAssessmentsAcquirerFee(double assessments_acquirer_fee) {
        this.assessments_acquirer_fee = assessments_acquirer_fee;
    }

    public double getOtherFees() {
        return other_fees;
    }

    public void setOtherFees(double other_fees) {
        this.other_fees = other_fees;
    }

    public double getNetDeposit() {
        return net_deposit;
    }

    public void setNetDeposit(double net_deposit) {
        this.net_deposit = net_deposit;
    }

}
