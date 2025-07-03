package org.example.ecommerce.controller;

import org.example.ecommerce.model.CartItem;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @GetMapping
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody CartItem item, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        cart.add(item);
        session.setAttribute("cart", cart);
        return "Added to cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.setAttribute("cart", new ArrayList<CartItem>());
        return "Cart cleared";
    }
}