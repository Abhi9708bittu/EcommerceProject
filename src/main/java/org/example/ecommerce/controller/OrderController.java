package org.example.ecommerce.controller;

import org.example.ecommerce.model.CartItem;
import org.example.ecommerce.model.Order;
import org.example.ecommerce.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String username, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "Cart is empty";
        }
        List<Long> productIds = cart.stream().map(CartItem::getProductId).collect(Collectors.toList());
        Order order = new Order(username, productIds);
        orderRepository.save(order);
        session.setAttribute("cart", new ArrayList<CartItem>());
        return "Order placed successfully";
    }
}