package com.unibuc.java_project.controller;
import com.unibuc.java_project.controller.OrderController;
import com.unibuc.java_project.dto.DishTopDTO;
import com.unibuc.java_project.dto.OrderDTO;
import com.unibuc.java_project.dto.OrderToPlaceDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.exceptions.UnavailableException;
import com.unibuc.java_project.model.PaymentMethod;
import com.unibuc.java_project.service.OrderService; // Import the service

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.unibuc.java_project.model.Order;

import java.util.List;


@Controller
@RequestMapping("/orders")
public class OrderPageController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showOrdersPage(Model model) {
        return "orders";
    }
//
//    @GetMapping("/top-dishes")
//    @ResponseBody
//    public List<DishTopDTO> getTopDishes() {
//        return orderService.getTop5MostOrderedDishes();
//    }



//    @GetMapping("/top-dishes")
//    public String showTopDishesPage(Model model) {
//        // Fetch the top dishes from the service
//        List<DishTopDTO> topDishes = orderService.getTop5MostOrderedDishes();
//
//        // Add the list to the Thymeleaf model
//        model.addAttribute("topDishes", topDishes);
//
//        // Return the name of the Thymeleaf template to be rendered
//        return "topDishes";
//    }
    @GetMapping("/top-dishes")
    public String showTopDishesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderCount") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model) {

        // Fetch paginated and sorted dishes from the service
        Page<DishTopDTO> topDishesPage = orderService.getTopOrderedDishes(page, size, sortBy, direction);

        // Add to the model
        model.addAttribute("topDishes", topDishesPage.getContent());
        model.addAttribute("currentPage", topDishesPage.getNumber());
        model.addAttribute("totalPages", topDishesPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        // Render the view
        return "topDishes";
    }




    @PostMapping("/place")
    @ResponseBody
    public ResponseEntity<?> placeOrder(@RequestBody OrderToPlaceDTO orderDTO) {
        try {
            OrderDTO placedOrder = orderService.placeOrder(orderDTO);
            return ResponseEntity.ok(placedOrder);
        } catch (ResourceNotFoundException | UnavailableException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/place")
    public String showPlaceOrderForm(Model model) {
        model.addAttribute("orderDTO", new OrderToPlaceDTO());
        return "orders/forms/placeOrderForm";
    }


    @GetMapping("/update")
    public String showUpdateOrderForm(Model model) {
        return "orders/forms/updateOrderForm";
    }

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        try {
            OrderDTO orderDTO = orderService.getOrderById(id);
            model.addAttribute("order", orderDTO);
            return "orderDetails";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Order not found");
            return "redirect:/orders";
        }
    }



    @GetMapping("/get")
    public String showGetOrderForm(Model model) {
        return "orders/forms/getOrderForm";
    }

    @GetMapping("/check/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> checkOrderExists(@PathVariable Long id) {
        try {
            orderService.getOrderById(id);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }


    @PostMapping("/update")
    public String updateOrder(
            @RequestParam Long orderId,
            @RequestParam Integer status,
            @RequestParam PaymentMethod paymentMethod,
            Model model) {
        try {
            // Call the service layer to update the order status and payment method
            orderService.updateOrderStatus(orderId, status, paymentMethod);

            // Redirect to the orders page with a success message
            model.addAttribute("message", "Order updated successfully!");
            return "redirect:/orders";
        } catch (ResourceNotFoundException e) {
            // Add error message and return to the update form
            model.addAttribute("error", "Order not found: ID " + orderId + " does not exist");
            return "orders/forms/updateOrderForm";
        }
    }
}


