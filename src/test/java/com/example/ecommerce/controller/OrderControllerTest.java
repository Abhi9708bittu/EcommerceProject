package com.example.ecommerce.controller;

import org.example.ecommerce.controller.OrderController;
import org.example.ecommerce.model.CartItem;
import org.example.ecommerce.model.Order;
import org.example.ecommerce.repository.OrderRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderController orderController;
    private MockHttpSession session;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderController = new OrderController(orderRepository, productRepository);
        session = new MockHttpSession();
    }

    @Test
    void test_checkout_cart_empty() {
        session.setAttribute("cart", new ArrayList<CartItem>());
        OrderController.OrderSummary summary = orderController.checkout("user", "UPI", session);
        assertEquals("Cart is empty", summary.message);
    }

    @Test
    void test_checkout_success() {
        List<CartItem> cart = Arrays.asList(new CartItem(1L, 2), new CartItem(2L, 1));
        session.setAttribute("cart", cart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(new org.example.ecommerce.model.Product(1L, "P1", "D1", 10.0)));
        when(productRepository.findById(2L)).thenReturn(Optional.of(new org.example.ecommerce.model.Product(2L, "P2", "D2", 20.0)));
        doAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L);
            return null;
        }).when(orderRepository).save(any(Order.class));
        OrderController.OrderSummary summary = orderController.checkout("user", "COD", session);
        assertEquals("Order placed successfully", summary.message);
        assertEquals("user", summary.username);
        assertEquals("COD", summary.paymentMethod);
        assertEquals(40.0, summary.total);
        assertNotNull(summary.orderId);
    }

    @Test
    void test_get_order_summary_found() {
        Order order = new Order(1L, "user", Arrays.asList(1L, 2L), "UPI", 50.0);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderController.OrderSummary summary = orderController.getOrderSummary(1L);
        assertEquals("Order found", summary.message);
        assertEquals(1L, summary.orderId);
        assertEquals("user", summary.username);
        assertEquals("UPI", summary.paymentMethod);
        assertEquals(50.0, summary.total);
    }

    @Test
    void test_get_order_summary_not_found() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());
        OrderController.OrderSummary summary = orderController.getOrderSummary(2L);
        assertEquals("Order not found", summary.message);
        assertNull(summary.orderId);
    }
} 