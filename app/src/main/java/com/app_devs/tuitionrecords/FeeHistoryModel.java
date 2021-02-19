package com.app_devs.tuitionrecords;

public class FeeHistoryModel {
    String amount, date;

    public FeeHistoryModel()
    {

    }

    public FeeHistoryModel(String amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
