package com.expense.expense_ui;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class ExpenseController {
    @Autowired
    private LoginCredentials loginCredentials;
    private Expense expense;

    private int userID;

    @GetMapping("/")
    public String showLoginForm() {
        return "login"; // This will return the login.html Thymeleaf template
    }

    @PostMapping("/login")
    public String processLoginForm(@RequestParam String username, @RequestParam String password, Model model) {
        // Call the loginStatus method from the LoginCredentials class
        int[]loginSuccessful = loginCredentials.loginStatus(username,password);

        if (loginSuccessful[0] == 1 ) {
            // Add the login information to the model to display it in another view
            userID = loginSuccessful[1];
            return "expenses";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/login")
    public String addPieElements(Model model) {
        // Assuming expense.getAllCategoriesAndValues(userId) returns a Map<String, Double> where the key is the category name and the value is the corresponding value
        Map<String, Double> categoryData = expense.getAllCategories(userID);

        List<String> categoryLabels = new ArrayList<>();
        List<Double> categoryValues = new ArrayList<>();

        for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
            categoryLabels.add(entry.getKey());
            categoryValues.add(entry.getValue());
        }

        model.addAttribute("categoryLabels", categoryLabels);
        model.addAttribute("categoryValues", categoryValues);

        return "expenses";
    }


  

   
}


