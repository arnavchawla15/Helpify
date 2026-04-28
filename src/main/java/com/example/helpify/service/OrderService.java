package com.example.helpify.service;

import com.example.helpify.entity.Order;
import com.example.helpify.entity.User;
import com.example.helpify.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order, User user) {
        order.setPostedBy(user.getEmail());
        order.setCreatedAt(new Date());
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order acceptOrder(String id, String email, String name) {
        Order o = orderRepository.findById(id).orElseThrow();
        o.setStatus("ACCEPTED");
        o.setAcceptedBy(email);
        o.setAcceptedByName(name);
        return orderRepository.save(o);
    }

    public Order completeOrder(String id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus("DELIVERED");
        return orderRepository.save(order);
    }
    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
    public List<Order> getNearbyOrders(User user) {

        List<Order> allOrders = orderRepository.findAll();

        return allOrders.stream()
                .filter(order -> distance(
                        user.getLatitude(),
                        user.getLongitude(),
                        order.getLat(),
                        order.getLng()
                ) < 1.0) // 1 km radius
                .toList();
    }
    public Order cancelOrder(String id, String email) {
        Order o = orderRepository.findById(id).orElseThrow();

        if (!email.equals(o.getAcceptedBy())) {
            throw new RuntimeException("Not your order");
        }

        o.setStatus("POSTED");
        o.setAcceptedBy(null);
        o.setAcceptedByName(null);

        return orderRepository.save(o);
    }

}
