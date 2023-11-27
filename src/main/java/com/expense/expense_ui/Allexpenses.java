package com.expense.expense_ui;

public class Allexpenses {
    private String category;
    private double amount;
    private String purchase_date;
    private String descriptions;

    public Allexpenses(String category, double amount, String purchases_date, String descriptions){
        this.category = category;
        this.amount = amount;
        this.purchase_date = purchases_date;
        this.descriptions = descriptions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getDescriptions() {
        return descriptions;
    }


    
    
}
