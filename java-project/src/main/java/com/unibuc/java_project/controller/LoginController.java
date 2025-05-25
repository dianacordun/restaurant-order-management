package com.unibuc.java_project.controller;

import com.unibuc.java_project.model.User;
import com.unibuc.java_project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        List<String> serviceErrors = userService.registerUser(user);
        if (!serviceErrors.isEmpty()) {
            for (String err : serviceErrors) {
                bindingResult.reject("0", err);
            }
            return "signup";
        }

        return "redirect:/login?signupSuccess=true";
    }
}
