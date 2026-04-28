package com.example.helpify.controller;

import com.example.helpify.entity.Order;
import com.example.helpify.entity.User;
import com.example.helpify.service.OrderService;
import com.example.helpify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ===== CREATE ORDER =====
    @PostMapping
    public Order create(@RequestBody Order order, HttpServletRequest req) {
        String email = (String) req.getAttribute("userEmail");

        if (email == null) {
            throw new RuntimeException("Not logged in");
        }

        return orderService.createOrder(order, email);
    }

    // ===== GET ALL ORDERS =====
    @GetMapping
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    // ===== ACCEPT ORDER =====
    @PutMapping("/{id}/accept")
    public Order accept(@PathVariable String id, HttpServletRequest req) {

        String email = (String) req.getAttribute("userEmail");

        if (email == null) {
            throw new RuntimeException("Not logged in");
        }

        User user = userService.findByEmail(email);

        return orderService.acceptOrder(id, email, user.getUsername());
    }
    // ===== COMPLETE ORDER =====
    @PutMapping("/{id}/complete")
    public Order complete(@PathVariable String id, HttpServletRequest req) {

        String email = (String) req.getAttribute("userEmail");

        if (email == null) {
            throw new RuntimeException("Not logged in");
        }

        return orderService.completeOrder(id);
    }

    // ===== CANCEL ORDER =====
    @PutMapping("/{id}/cancel")
    public Order cancel(@PathVariable String id, HttpServletRequest req) {

        String email = (String) req.getAttribute("userEmail");

        if (email == null) {
            throw new RuntimeException("Not logged in");
        }

        return orderService.cancelOrder(id, email);
    }
    // ===== NEARBY ORDERS =====
    @GetMapping("/nearby")
    public List<Order> getNearby(HttpServletRequest req) {

        String email = (String) req.getAttribute("userEmail");

        if (email == null) {
            throw new RuntimeException("Not logged in");
        }

        User user = userService.findByEmail(email);

        return orderService.getNearbyOrders(user);
    }
}