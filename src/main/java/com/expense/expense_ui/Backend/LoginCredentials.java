package com.expense.expense_ui.Backend;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class LoginCredentials {


    public LoginCredentials(){

    }


    public int[] loginStatus(String usernameToCheck, String passwordToCheck) {
        String SQL = "SELECT login_id FROM Login WHERE user_name = ? AND password = ?";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");
    
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setString(1, usernameToCheck);
            preparedStatement.setString(2, passwordToCheck);
    
            ResultSet result = preparedStatement.executeQuery();
    
            if (result.next()) {
                int loginID = result.getInt("login_id");
                int[]temp = {1,loginID};
                return temp;
            }

    
            preparedStatement.close();
            dbConnection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }
        int[] temp = {0, -1};
        return temp; 
    }


}
