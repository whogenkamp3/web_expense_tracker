package com.expense.expense_ui;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ExpenseOverview {
    private double total;
    private int fk_login_id;
    private int numberOfExpenses;
    private ArrayList<String> category;
    private ArrayList<Double> total_amount;

    public ExpenseOverview(){

    }

    public double getTotal(){
        return total;

    }

    public ArrayList<Double> getTotalAmount(){
        return total_amount;
    }

    public ArrayList<String> getAllCategories(){
        return category;
    }

    public void addExpense(String category, double total, String comment, Date date){
        //will eventually pull date from model
        Expense expense = new Expense(this.numberOfExpenses,category,total,date,comment,this.fk_login_id);
        
     

    }


    public void loadCategories(int userID){
        String SQL = "SELECT  category, SUM(amount) as totalAmount FROM Expense WHERE fk_login_id = ? GROUP BY category WITH ROLLUP;";
        this.fk_login_id = userID;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setLong(1, userID);

            ResultSet result = preparedStatement.executeQuery();

            ArrayList<String>tempStringArray = new ArrayList<>();
            ArrayList<Double>tempDoubleArray = new ArrayList<>();

            
            while(result.next()){
                String tempString = result.getString("category");
                double tempDouble =  result.getDouble("totalAmount");
                tempStringArray.add(tempString);
                tempDoubleArray.add(tempDouble);
            }


            this.total = tempDoubleArray.get(tempDoubleArray.size()-1);

            this.numberOfExpenses = tempDoubleArray.size() -1;

            this.category = new ArrayList<>(tempStringArray.subList(0, tempStringArray.size() - 1));
            this.total_amount = new ArrayList<>(tempDoubleArray.subList(0,tempDoubleArray.size() -1));

            preparedStatement.close();
            dbConnection.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[]args){
        ExpenseOverview expense = new ExpenseOverview();
        expense.loadCategories(1);


    }


  
}
