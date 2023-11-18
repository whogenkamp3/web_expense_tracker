package com.expense.expense_ui;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Expense {

    public Expense(){

    }

    public Map<String,Double> getAllCategories(int userID){
        String SQL = "SELECT category, SUM(amount) as totalAmount FROM Expense WHERE fk_login_id = ? GROUP BY category;";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setLong(1, userID);

            ResultSet result = preparedStatement.executeQuery();
            
            //ArrayList<String> temp = new ArrayList<>();
            Map<String, Double> temp = new HashMap<>();
            while(result.next()){
                String tempString = result.getString("category");
                double tempDouble =  result.getDouble("totalAmount");
                temp.put(tempString,tempDouble);
            }
            preparedStatement.close();
            dbConnection.close();
            return temp;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }


        return new HashMap<>();

    }


  
}
