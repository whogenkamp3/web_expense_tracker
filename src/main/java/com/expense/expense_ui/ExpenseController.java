package com.expense.expense_ui;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ExpenseController {

    private ArrayList<String> categoryLabels;
    private ArrayList<Double> categoryValues;
    
    private int userID;

    @GetMapping("/")
    public String showLoginForm() {
        return "login"; 
    }

    @GetMapping("/expenses")
    public String homePage(Model model){
        model.addAttribute("categoryLabels", categoryLabels);
        model.addAttribute("categoryValues", categoryValues);
        return "expenses";
    }

    @GetMapping("/addExpense")
    public String addingExpense(Model model){
        return "addExpense";
    }

    @PostMapping("/login")
    public String processLoginForm(@RequestParam String username, @RequestParam String password, Model model) {
        LoginCredentials loginCredential = new LoginCredentials();
        int[]loginSuccessful = loginCredential.loginStatus(username,password);

        if (loginSuccessful[0] == 1 ) {
            userID = loginSuccessful[1];
            ExpenseOverview expense = new ExpenseOverview();
            expense.loadCategories(userID);


            ArrayList<String> categoryLabels = new ArrayList<>();
            ArrayList<Double> categoryValues = new ArrayList<>();

            categoryLabels = expense.getAllCategories();
            categoryValues = expense.getTotalAmount();

            if (expense.getAllCategories() == null || expense.getTotalAmount() == null) {
                model.addAttribute("categoryLabels", categoryLabels);
                model.addAttribute("categoryValues", categoryValues);
                this.categoryLabels = categoryLabels;
                this.categoryValues = categoryValues;
                return "expenses";
            }


            double total = expense.getTotal();

            for(int i=0;i<categoryValues.size();i++){
                double temp = (categoryValues.get(i) / total) * 100;
                double roundedValue = Math.round(temp * 100.0) / 100.0;
                categoryValues.set(i,roundedValue);
            }

            model.addAttribute("categoryLabels", categoryLabels);
            model.addAttribute("categoryValues", categoryValues);

            this.categoryLabels = categoryLabels;
            this.categoryValues = categoryValues;

            return "expenses";
        } else {
            return "redirect:/";
        }
    }



  


  

   
}


