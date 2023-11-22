package com.expense.expense_ui;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Expense {
    private int expense_id;
    private String category;
    private double cost;
    private Date date;
    private String comment;
    private int fk_login_id;

    public Expense(){

    }

    public Expense(int expense_id, String category, double cost, java.util.Date date2, String comment, int fk_login_id){
        this.expense_id = expense_id;
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.comment = comment;
        this.fk_login_id = fk_login_id;
    }

    public boolean addToBackEnd(){
        String SQL = "INSERT INTO Expense VALUES (?,?,?,?,?,?);";

    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setInt(1, this.expense_id);
            preparedStatement.setString(2, this.category);
            preparedStatement.setDouble(3, this.cost);
            preparedStatement.setDate(4, this.date);
            preparedStatement.setString(5, this.comment);
            preparedStatement.setInt(6, this.fk_login_id);

            ResultSet result = preparedStatement.executeQuery();


            while(result.next()){
                return true;
            }


            preparedStatement.close();
            dbConnection.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void main(String[]args){
        String day = "2022-05-12";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Expense expense = new Expense(4,"furniture",300,dateFormat.parse(day),"purchasing more furniture for office",1);
    }


    
}
