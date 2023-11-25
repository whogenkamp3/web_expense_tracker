package com.expense.expense_ui;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;




@Controller
public class ExpenseController {

    @GetMapping("/")
    public String showLoginForm() {
        return "login"; 
    }

    @GetMapping("/expenses")
    public String homePage(@CookieValue(name = "categoryValues", defaultValue = "") String serializedValues, @CookieValue(name = "categoryLabels", defaultValue = "") String serializedCategories,Model model){
        List<String> categoryValues = getUnserializedData(serializedValues);
        List<String> categoryLabels = getUnserializedData(serializedCategories);
        model.addAttribute("categoryLabels", categoryLabels);
        model.addAttribute("categoryValues", categoryValues);
        return "expenses";
    }

    @GetMapping("/addExpense")
    public String addingExpense(Model model){
        return "addExpense";
    }

    @PostMapping("/addSucessful")
    public String processAddForm(HttpServletResponse response,@CookieValue(name="categoryTotals",defaultValue = "")String serializedTotals,@CookieValue(name="total",defaultValue = "")String tempTotal,@CookieValue(name = "categoryValues", defaultValue = "") String serializedValues, @CookieValue(name = "categoryLabels", defaultValue = "") String serializedCategories, @CookieValue(name = "userID", defaultValue = "") String tempUserID, @RequestParam String category, @RequestParam double cost, @RequestParam String date, @RequestParam String comment,Model model){
        ExpenseOverview expense = new ExpenseOverview();
        int userID = Integer.parseInt(tempUserID);
        double total = Double.parseDouble(tempTotal);
        List<String> categoryLabels = new ArrayList<>();
        List<String> tempcategoryValues = new ArrayList<>();
        List<String> tempcategoryTotals = new ArrayList<>();
        boolean emptyArrays = true;

        if(!serializedCategories.isEmpty() && ! serializedValues.isEmpty() && !serializedTotals.isEmpty()){
            categoryLabels = getUnserializedData(serializedCategories);
            tempcategoryValues = getUnserializedData(serializedValues);
            tempcategoryTotals = getUnserializedData(serializedTotals);
            emptyArrays = false;
        }

        List<Double> categoryValues = new ArrayList<>();
        List<Double> categoryTotals = new ArrayList<>();
        for(int i=0;i<tempcategoryValues.size() && !emptyArrays;i++){
            double temp = Double.parseDouble(tempcategoryValues.get(i));
            double tempOne = Double.parseDouble(tempcategoryTotals.get(i));
            categoryValues.add(temp);
            categoryTotals.add(tempOne);
        }
        
        expense.addExpense(userID,category, cost, comment, date);

        boolean newCategory = true;
        int index = 0;
        if(categoryLabels != null){
            for(int i=0; i< categoryLabels.size();i++){
                if(categoryLabels.get(i).equals(category)){
                    index = i;
                    newCategory = false;
                }
            }
        }
        total = total + cost;

        if(emptyArrays){
            List<String> tempcategoryLabels = new ArrayList<>();
            tempcategoryLabels.add(category);
            model.addAttribute("categoryLabels", tempcategoryLabels);
            List<Double> tempTwocategoryValues = new ArrayList<>();
            tempTwocategoryValues.add(cost / total);
            model.addAttribute("categoryValues", tempTwocategoryValues);
            List<Double> tempTwocategoryTotals = new ArrayList<>();
            tempTwocategoryTotals.add(cost);
            updateCookies(response, total, tempcategoryLabels, tempTwocategoryValues,tempTwocategoryTotals);
        }
        else if(newCategory && !emptyArrays && categoryLabels != null && categoryValues != null){
            List<String> tempStringdata = serializeData(categoryLabels,category);
            List<Double> tempTotaldata = serializeData(categoryTotals,cost);
            double tempCost = cost / total;
            List<Double> tempDoubledata = serializePercentage(categoryValues,categoryTotals,tempCost,total);
            model.addAttribute("categoryLabels", tempStringdata);
            model.addAttribute("categoryValues", tempDoubledata);
            updateCookies(response, total, tempStringdata, tempDoubledata,tempTotaldata);
        }
        else{
            model.addAttribute("categoryLabels", categoryLabels);
            for(int i=0;i<categoryValues.size();i++){
                if(i==index){
                    categoryValues.set(i,categoryValues.get(i)+cost);
                }
                //categoryValues.set(i,);
            }
            model.addAttribute("categoryValues", categoryValues);
            updateTotalCookie(response,total);
        }

        return "expenses";
    }

    private void updateCookies(HttpServletResponse response, double total, List<String> categoryLabels, List<Double> categoryValues, List<Double> categoryTotals) {
        Cookie totalCookie = new Cookie("total", String.valueOf(total));
        response.addCookie(totalCookie);

        String tempJoin = String.join(",", categoryLabels);

        Cookie categoryLabelsCookie = new Cookie("categoryLabels", URLEncoder.encode(tempJoin, StandardCharsets.UTF_8));
        response.addCookie(categoryLabelsCookie);

        List<String> temp = new ArrayList<>();
        List<String> tempOne = new ArrayList<>();
        for(int i=0;i<categoryValues.size();i++){
            temp.add(categoryValues.get(i)+"");
            tempOne.add(categoryTotals.get(i)+"");
        }

        String tempArrayJoin = String.join(",", temp);
        String tempArrayJoinTwo = String.join(",", tempOne);
        Cookie categoryValuesCookie = new Cookie("categoryValues", URLEncoder.encode(tempArrayJoin, StandardCharsets.UTF_8));
        Cookie CategoryTotalsCookie = new Cookie("categoryTotals",URLEncoder.encode(tempArrayJoinTwo, StandardCharsets.UTF_8));
        response.addCookie(categoryValuesCookie);
        response.addCookie(CategoryTotalsCookie);
    }

    private void updateTotalCookie(HttpServletResponse response, double total) {
        Cookie totalCookie = new Cookie("total", String.valueOf(total));
        response.addCookie(totalCookie);
    }

    private List<String> getUnserializedData(String temp){
        List<String> tempArray = Arrays.asList(temp.split(","));
        return tempArray;
    }

    private List<String> serializeData(List<String>tempArray, String temp){
        List<String>newArray = new ArrayList<>();
        List<String>newTempArray = new ArrayList<>();
        for(int i=0;i<tempArray.size();i++){
            newTempArray.add(tempArray.get(i));
        }
        newArray = newTempArray;
        newArray.add(temp);
        return newArray;

    }

    private List<Double> serializeData(List<Double> totals, double tempCost) {
        List<Double>newArray = new ArrayList<>();
        List<Double>newTempArray = new ArrayList<>();
        for(int i=0;i<totals.size();i++){
            newTempArray.add(totals.get(i));
        }
        newArray = newTempArray;
        newArray.add(tempCost);
        return newArray;
    }

    private List<Double> serializePercentage(List<Double> categoryValues,List<Double>categoryTotals, double tempCost,double total){
        List<Double>newArray=new ArrayList<>();
        List<Double>newTempArray = new ArrayList<>();
        for(int i=0;i<categoryValues.size();i++){
            double temp = (categoryTotals.get(i)/total)*100;
            double rounded = Math.round(temp*100.0)/100.0;
            newTempArray.add(rounded);
        }
        newArray = newTempArray;
        tempCost = tempCost*100;
        double roundedValue = Math.round(tempCost * 100.0) / 100.0;
        newArray.add(roundedValue);
        return newArray;
    }

    @PostMapping("/login")
    public String processLoginForm( @RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response) {
        LoginCredentials loginCredential = new LoginCredentials();
        int[]loginSuccessful = loginCredential.loginStatus(username,password);

        if (loginSuccessful[0] == 1 ) {
            ExpenseOverview expense = new ExpenseOverview();
            int userID = loginSuccessful[1];
            expense.loadCategories(userID);


            List<String> categoryLabels = new ArrayList<>();
            List<Double> categoryValues = new ArrayList<>();
            List<Double> categoryTotals = new ArrayList<>();

            categoryLabels = expense.getAllCategories();
            categoryTotals = expense.getTotalAmount();

            if (expense.getAllCategories() == null || expense.getTotalAmount() == null) {
                model.addAttribute("categoryLabels", categoryLabels);
                model.addAttribute("categoryValues", categoryTotals);
                String tempUser = userID + "";
                Cookie cookieOne = new Cookie("userID", tempUser);
                Cookie cookieTwo = new Cookie("total", "0");
                Cookie cookieThree = new Cookie("categoryLabels","");
                Cookie cookieFour = new Cookie("categoryValues", "");
                Cookie cookieFive = new Cookie("categoryTotals","");
                response.addCookie(cookieOne);
                response.addCookie(cookieTwo);
                response.addCookie(cookieThree);
                response.addCookie(cookieFour);
                response.addCookie(cookieFive);
                return "expenses";
            }

            double total = expense.getTotal();
            String temptotal = total + "";
            Cookie cookieOne = new Cookie("total", temptotal);
            response.addCookie(cookieOne);

            for(int i=0;i<categoryTotals.size();i++){
                double temp = (categoryTotals.get(i) / total) * 100;
                double roundedValue = Math.round(temp * 100.0) / 100.0;
                categoryValues.add(roundedValue);
            }

            String tempUser = userID + "";
            Cookie cookieTwo = new Cookie("userID", tempUser);
            response.addCookie(cookieTwo);

            String serializedCategories = String.join(",", categoryLabels);
            Cookie cookieThree = new Cookie("categoryLabels",URLEncoder.encode(serializedCategories, StandardCharsets.UTF_8));
            response.addCookie(cookieThree);

            ArrayList<String>tempValues = new ArrayList<>();
            ArrayList<String>tempValueTotals = new ArrayList<>();
            for(int i=0;i<categoryValues.size();i++){
                tempValues.add(categoryValues.get(i) + "");
                tempValueTotals.add(categoryTotals.get(i)+"");
            }

            String serializedValues = String.join(",",tempValues);
            String serializedTotalValues = String.join(",",tempValueTotals);
            Cookie cookieFour = new Cookie("categoryValues", URLEncoder.encode(serializedValues, StandardCharsets.UTF_8));
            Cookie cookieFive = new Cookie("categoryTotals", URLEncoder.encode(serializedTotalValues, StandardCharsets.UTF_8));
            response.addCookie(cookieFour);
            response.addCookie(cookieFive);



            model.addAttribute("categoryLabels", categoryLabels);
            model.addAttribute("categoryValues", categoryValues);

            return "expenses";
        } else {
            return "redirect:/";
        }
    }



  


  

   
}


