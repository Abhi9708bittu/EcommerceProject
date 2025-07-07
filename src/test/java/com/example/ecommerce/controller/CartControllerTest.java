package com.example.ecommerce.controller;

import org.example.ecommerce.controller.CartController;
import org.example.ecommerce.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartControllerTest {
    private CartController cartController;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        cartController = new CartController();
        session = new MockHttpSession();
    }

    @Test
    void test_add_and_view_cart() {
        CartItem item = new CartItem(1L, 2);
        cartController.addToCart(item, session);
        List<CartItem> cart = cartController.getCart(session);
        assertEquals(1, cart.size());
        assertEquals(1L, cart.get(0).getProductId());
        assertEquals(2, cart.get(0).getQuantity());
    }

    @Test
    void test_update_cart_item() {
        CartItem item = new CartItem(1L, 2);
        cartController.addToCart(item, session);
        CartItem updated = new CartItem(1L, 5);
        String result = cartController.updateCartItem(updated, session);
        assertEquals("Cart item updated", result);
        List<CartItem> cart = cartController.getCart(session);
        assertEquals(5, cart.get(0).getQuantity());
    }

    @Test
    void test_remove_from_cart() {
        CartItem item = new CartItem(1L, 2);
        cartController.addToCart(item, session);
        String result = cartController.removeFromCart(item, session);
        assertEquals("Item removed from cart", result);
        List<CartItem> cart = cartController.getCart(session);
        assertTrue(cart.isEmpty());
    }

    @Test
    void test_clear_cart() {
        cartController.addToCart(new CartItem(1L, 2), session);
        cartController.addToCart(new CartItem(2L, 3), session);
        String result = cartController.clearCart(session);
        assertEquals("Cart cleared", result);
        List<CartItem> cart = cartController.getCart(session);
        assertTrue(cart.isEmpty());
    }
} 