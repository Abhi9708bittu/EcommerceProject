package org.example.ecommerce.controller;

import org.example.ecommerce.model.CartItem;
import org.example.ecommerce.model.Order;
import org.example.ecommerce.repository.OrderRepository;
import org.example.ecommerce.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/checkout")
    public OrderSummary checkout(@RequestParam String username, @RequestParam String paymentMethod, HttpSession session) {
        List<CartItem> cart = getCartFromSession(session);
        if (cart == null || cart.isEmpty()) {
            return new OrderSummary("Cart is empty");
        }
        List<Long> productIds = cart.stream().map(CartItem::getProductId).collect(Collectors.toList());
        double total = cart.stream()
                .mapToDouble(item -> productRepository.findById(item.getProductId()).map(p -> p.getPrice() * item.getQuantity()).orElse(0.0))
                .sum();
        Order order = new Order(username, productIds, paymentMethod, total);
        orderRepository.save(order);
        session.setAttribute("cart", new ArrayList<CartItem>());
        return new OrderSummary(order.getId(), username, productIds, paymentMethod, total, "Order placed successfully");
    }

    @GetMapping("/summary/{orderId}")
    public OrderSummary getOrderSummary(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> new OrderSummary(order.getId(), order.getUsername(), order.getProductIds(), order.getPaymentMethod(), order.getTotal(), "Order found"))
                .orElse(new OrderSummary("Order not found"));
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCartFromSession(HttpSession session) {
        Object cartObj = session.getAttribute("cart");
        if (cartObj instanceof List<?>) {
            List<?> list = (List<?>) cartObj;
            if (list.isEmpty() || list.get(0) instanceof CartItem) {
                return (List<CartItem>) list;
            }
        }
        return null;
    }

    public static class OrderSummary {
        public Long orderId;
        public String username;
        public List<Long> productIds;
        public String paymentMethod;
        public double total;
        public String message;
        public OrderSummary(Long orderId, String username, List<Long> productIds, String paymentMethod, double total, String message) {
            this.orderId = orderId;
            this.username = username;
            this.productIds = productIds;
            this.paymentMethod = paymentMethod;
            this.total = total;
            this.message = message;
        }
        public OrderSummary(String message) {
            this.message = message;
        }
    }
}