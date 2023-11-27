package com.expense.expense_ui;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ExpenseOverview {
    private double total;
    private ArrayList<String> category;
    private ArrayList<Double> total_amount;
    private List<String> expenseDates;
    private List<String> expenseCategories;
    private List<Double> expenseAmounts; 
    private List<Integer> expense_id;

    public ExpenseOverview(){

    }

    public ExpenseOverview(List<Integer> expense_id,List<String> expenseDates,List<String> expenseCategories,List<Double> expenseAmounts){
        this.expenseDates = expenseDates;
        this.expenseCategories = expenseCategories;
        this.expenseAmounts = expenseAmounts;
        this.expense_id = expense_id;
    }

    public List<String> getExpenseDates(){
        return expenseDates;
    }

    public List<String> getExpenseCategories(){
        return expenseCategories;
    }

    public List<Double> getExpenseAmounts(){
        return expenseAmounts;
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

    public List<Integer> getExpenseID(){
        return expense_id;
    }

    public List<Allexpenses> allExpensesForDisplay(int userID){
        String SQL = "SELECT category,amount,purchase_date,descriptions FROM expense WHERE fk_login_id = ?";
        List<Allexpenses> returnData = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setInt(1,userID);

            ResultSet result = preparedStatement.executeQuery();


            while(result.next()){ 
                String tempCat = result.getString("category");
                Double tempAmount = result.getDouble("amount");
                Date tempPurchase = result.getDate("purchase_date");
                String tempDate = tempPurchase + "";
                String tempDes = result.getString("descriptions");
                Allexpenses tempExpenses = new Allexpenses(tempCat, tempAmount, tempDate, tempDes);
                returnData.add(tempExpenses);
                
            }

            preparedStatement.close();
            dbConnection.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnData;

    }


    public ExpenseOverview dataForUpdateExpense(int UserID){
        String SQL = "SELECT expense_id, category, amount,purchase_date FROM Expense WHERE fk_login_id = ?";
        List<String> expenseDates = new ArrayList<>();
        List<Double> expenseAmounts = new ArrayList<>();
        List<String> expenseCategories = new ArrayList<>();
        List<Integer> expense_id = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setInt(1,UserID);

            ResultSet result = preparedStatement.executeQuery();


            while(result.next()){ 
                Date tempDate = result.getDate("purchase_date");
                String tempStringDate = tempDate.toString();
                double tempDouble = result.getDouble("amount");
                String tempString = result.getString("category");
                int tempExpenseID = result.getInt("expense_id");
                expenseDates.add(tempStringDate);
                expenseAmounts.add(tempDouble);
                expenseCategories.add(tempString);
                expense_id.add(tempExpenseID);
            }

            preparedStatement.close();
            dbConnection.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }

        ExpenseOverview returnData = new ExpenseOverview(expense_id, expenseDates,expenseCategories,expenseAmounts);
        return returnData;


    }

    public void updateExpense(int expense_id,String category, double cost, String date){
        String SQL = "UPDATE Expense SET";
        if(category.length() > 0){
            SQL = SQL + " category = " + '"' + category + '"' + ",";
        }
        if(cost != 0.0){
            SQL = SQL + " amount = " + cost + ",";
        }
        
        if(date.length() > 0){
            SQL = SQL + " purchase_date = STR_TO_DATE(?, '%Y-%m-%d'),";
        }

        int length = SQL.length();

        if(SQL.substring(length-1, length).equals(",")){
            SQL = SQL.substring(0,length-1);
        }

        SQL = SQL + " WHERE expense_id = ?;";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            if(date.length() > 0){
                date = "20" + date;
                preparedStatement.setString(1, date); 
                 preparedStatement.setInt(2, expense_id);  
            }
            else{
                preparedStatement.setInt(1, expense_id);
            }
            

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

    public void addExpense(int userID, String category, double total, String comment, String date){
        date = "20" + date;

        String SQL = "INSERT INTO Expense VALUES (?,?,?,?,?,?);";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");

            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            preparedStatement.setInt(1, getExpenseId() + 1);
            preparedStatement.setString(2, category);
            preparedStatement.setDouble(3, total);
            Date sqlDate=Date.valueOf(date);
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, comment);
            preparedStatement.setInt(6, userID);

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

    public int getExpenseId(){
        String SQL = "SELECT expense_id FROM expense ORDER BY expense_id DESC LIMIT 1;";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/expense_tracker_backend";
            Connection dbConnection = DriverManager.getConnection(dbURL, "root", "hkldDD3@78");
            PreparedStatement preparedStatement = dbConnection.prepareStatement(SQL);
            ResultSet result = preparedStatement.executeQuery();


            while(result.next()){
                return result.getInt("expense_id");
            }

            preparedStatement.close();
            dbConnection.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginCredentials.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }


    public void loadCategories(int userID){
        String SQL = "SELECT  category, SUM(amount) as totalAmount FROM Expense WHERE fk_login_id = ? GROUP BY category WITH ROLLUP;";
        
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

            if(tempDoubleArray.size() > 0 && tempStringArray.size() > 0){
                this.total = tempDoubleArray.get(tempDoubleArray.size()-1);
                this.category = new ArrayList<>(tempStringArray.subList(0, tempStringArray.size() - 1));
                this.total_amount = new ArrayList<>(tempDoubleArray.subList(0,tempDoubleArray.size() -1));
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
        ExpenseOverview expense = new ExpenseOverview();
        expense.loadCategories(1);



    }


  
}
