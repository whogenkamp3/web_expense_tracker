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
    public String homePage(Model model){
        return "expenses";
    }

    @GetMapping("/addExpense")
    public String addingExpense(Model model, LoginCredentials expense){
        return "addExpense";
    }

    @PostMapping("/addSucessful")
    public String processAddForm(@CookieValue(name="total",defaultValue = "")String tempTotal,@CookieValue(name = "categoryValues", defaultValue = "") String serializedValues, @CookieValue(name = "categoryLabels", defaultValue = "") String serializedCategories, @CookieValue(name = "userID", defaultValue = "") String tempUserID, @RequestParam String category, @RequestParam double cost, @RequestParam String date, @RequestParam String comment,Model model){
        ExpenseOverview expense = new ExpenseOverview();
        int userID = Integer.parseInt(tempUserID);
        double total = Double.parseDouble(tempTotal);
        List<String> categoryLabels = new ArrayList<>();
        List<String> tempcategoryValues = new ArrayList<>();
        boolean emptyArrays = true;

        if(!serializedCategories.isEmpty() && ! serializedValues.isEmpty()){
            categoryLabels = Arrays.asList(serializedCategories.split(","));
            tempcategoryValues = Arrays.asList(serializedValues.split(","));
            emptyArrays = false;
        }

        List<Double> categoryValues = new ArrayList<>();
        for(int i=0;i<tempcategoryValues.size() && !emptyArrays;i++){
            double temp = Double.parseDouble(tempcategoryValues.get(i));
            categoryValues.add(temp);
        }
        
        expense.addExpense(userID,category, cost, comment, date);

        boolean newCategory = true;
        if(categoryLabels != null){
            for(int i=0; i< categoryLabels.size();i++){
                if(categoryLabels.get(i).equals(category)){
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
        }
        else if(newCategory && !emptyArrays && categoryLabels != null && categoryValues != null){
            categoryLabels.add(category);
            categoryValues.add(cost/total);
            model.addAttribute("categoryLabels", categoryLabels);
            model.addAttribute("categoryValues", categoryValues);

        }
        else{
            model.addAttribute("categoryLabels", categoryLabels);
            model.addAttribute("categoryValues", categoryValues);
        }

        return "expenses";
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

            categoryLabels = expense.getAllCategories();
            categoryValues = expense.getTotalAmount();

            if (expense.getAllCategories() == null || expense.getTotalAmount() == null) {
                model.addAttribute("categoryLabels", categoryLabels);
                model.addAttribute("categoryValues", categoryValues);
                String tempUser = userID + "";
                Cookie cookieOne = new Cookie("userID", tempUser);
                Cookie cookieTwo = new Cookie("total", "0");
                Cookie cookieThree = new Cookie("categoryLabels","");
                Cookie cookieFour = new Cookie("categoryValues", "");
                response.addCookie(cookieOne);
                response.addCookie(cookieTwo);
                response.addCookie(cookieThree);
                response.addCookie(cookieFour);
                return "expenses";
            }

            double total = expense.getTotal();
            String temptotal = total + "";
            Cookie cookieOne = new Cookie("total", temptotal);
            response.addCookie(cookieOne);

            for(int i=0;i<categoryValues.size();i++){
                double temp = (categoryValues.get(i) / total) * 100;
                double roundedValue = Math.round(temp * 100.0) / 100.0;
                categoryValues.set(i,roundedValue);
            }

            String tempUser = userID + "";
            Cookie cookieTwo = new Cookie("userID", tempUser);
            response.addCookie(cookieTwo);

            String serializedCategories = String.join(",", categoryLabels);
            Cookie cookieThree = new Cookie("categoryLabels",URLEncoder.encode(serializedCategories, StandardCharsets.UTF_8));
            response.addCookie(cookieThree);

            ArrayList<String>tempValues = new ArrayList<>();
            for(int i=0;i<categoryValues.size();i++){
                tempValues.add(categoryValues.get(i) + "");
            }

            String serializedValues = String.join(",",tempValues);
            Cookie cookieFour = new Cookie("categoryValues", URLEncoder.encode(serializedValues, StandardCharsets.UTF_8));
            response.addCookie(cookieFour);


            model.addAttribute("categoryLabels", categoryLabels);
            model.addAttribute("categoryValues", categoryValues);

            return "expenses";
        } else {
            return "redirect:/";
        }
    }



  


  

   
}


