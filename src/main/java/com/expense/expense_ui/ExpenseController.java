package com.expense.expense_ui;



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

    @GetMapping("/")
    public String showLoginForm() {
        return "login"; // This will return the login.html Thymeleaf template
    }

    @PostMapping("/login")
    public String processLoginForm(@RequestParam String username, @RequestParam String password, Model model) {
        // Call the loginStatus method from the LoginCredentials class
        boolean loginSuccessful = loginCredentials.loginStatus(username, password);
        System.out.println(username + password);
        System.out.println(loginSuccessful);

        if (loginSuccessful) {
            // Add the login information to the model to display it in another view
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            return "expenses";
        } else {
            // Redirect back to the login page with an error message
            model.addAttribute("error", "Invalid credentials");
            return "redirect:/";
        }
    }

  

   
}


