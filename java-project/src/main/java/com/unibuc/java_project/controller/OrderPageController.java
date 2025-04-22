package com.unibuc.java_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderPageController {

    @GetMapping
    public String showOrdersPage(Model model) {
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        model.addAttribute("orderId", id);
        return "orderDetails";
    }
}
