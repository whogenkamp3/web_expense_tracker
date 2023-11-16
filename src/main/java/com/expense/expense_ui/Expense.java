package com.expense.expense_ui;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;   
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Expense {
    private int curr;
    private Integer id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private static double totalExpense;

    public Expense() {
        this.id = id ++;
        this.date = LocalDate.now(); // Default to current date
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCurrID(){
        return 1;
    }

    public double getTotalExpense(int login_id) {
        String SQL = "SELECT SUM(amount) AS total FROM Expense WHERE fk_login_id = ?";
        String login_id_String = login_id + "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setString(1, login_id_String);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                totalExpense = result.getDouble("total");
            }

            preparedStatement.close();
            dbConnection.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Expense.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Expense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return totalExpense;
    }

    
}
