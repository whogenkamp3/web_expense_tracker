package com.expense.expense_ui;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Expense {
    private int expense_id;
    private String category;
    private double cost;
    private String date;
    private String comment;
    private int fk_login_id;

    public Expense(){

    }

    public Expense(int expense_id, String category, double cost, String date, String comment, int fk_login_id){
        this.expense_id = expense_id;
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.comment = comment;
        this.fk_login_id = fk_login_id;
    }

    public void addToBackEnd(Expense expense){
        String SQL = "INSERT INTO Expense VALUES (?,?,?,?,?,?);";

    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setInt(1, expense_id);
            preparedStatement.setString(2, category);
            preparedStatement.setDouble(3, cost);

            for(int i =0; i<date.length();i++){
                System.out.println(date.substring(i, i +1));
            }



            Date sqlDate=Date.valueOf(date);




            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, comment);
            preparedStatement.setInt(6, fk_login_id);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Record inserted successfully!");
            } else {
                System.out.println("Failed to insert record.");
            }


            preparedStatement.close();
            dbConnection.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[]args){
        String today = "2011-11-12";
        Date sqlDate = Date.valueOf(today);
        Expense expense = new Expense(6,"furniture",300,today,"purchasing more furniture for office",2);
        
        expense.addToBackEnd(expense);
    }


    
}
