package com.expense.expense_ui;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class ExpenseController {

    private ArrayList<String> categoryLabels = new ArrayList<>();
    private ArrayList<Double> categoryValues = new ArrayList<>();
    private double total;
    
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

    @PostMapping("/addSucessful")
    public String processAddForm(@RequestParam String category, @RequestParam double cost, @RequestParam String date, @RequestBody String comment){
        ExpenseOverview expense = new ExpenseOverview();
        expense.addExpense(category, cost, comment, date);

        boolean newCategory = true;
        if(categoryLabels != null){
            for(int i=0; i< this.categoryLabels.size();i++){
                if(this.categoryLabels.get(i).equals(category)){
                    newCategory = false;
                }
            }
        }
        this.total = this.total + cost;

        if(newCategory){
            this.categoryLabels.add(category);
            this.categoryValues.add(cost / this.total);
        }
        


        return "expenses";
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
            this.total = total;

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


