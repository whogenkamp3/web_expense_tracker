package com.expense.expense_ui;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Expense {
    private int id;
    private String category;
    private Date day;
    private double cost;
    private String description;

    public Expense(){

    }

    public String[] getAllCategories(int userID){
        String SQL = "SELECT DISTINCT category FROM Expense WHERE fk_login_id = ?;";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setLong(1, userID);

            ResultSet result = preparedStatement.executeQuery();

            int i = 0;
            String[]temp = {};
            while(result.next()){
                String tempString = result.getString("fk_login_id");
                temp[i] = tempString;
                i ++;
            }
            preparedStatement.close();
            dbConnection.close();
            return temp;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }


        String []temp = {};
        return temp;

    }

    public static void main(String[]args){
        Expense ex = new Expense();
        String[]temp = ex.getAllCategories(1);
        for(int i=0; i < temp.length;i++){
            System.out.println(temp[i]);
        }
    }

  
}
