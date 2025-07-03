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
        List<CartItem> cart = getCartFromSession(session);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestBody CartItem item, HttpSession session) {
        List<CartItem> cart = getCartFromSession(session);
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

    @PostMapping("/remove")
    public String removeFromCart(@RequestBody CartItem item, HttpSession session) {
        List<CartItem> cart = getCartFromSession(session);
        if (cart == null) {
            return "Cart is empty";
        }
        boolean removed = cart.removeIf(ci -> ci.getProductId() == item.getProductId());
        session.setAttribute("cart", cart);
        return removed ? "Item removed from cart" : "Item not found in cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestBody CartItem item, HttpSession session) {
        List<CartItem> cart = getCartFromSession(session);
        if (cart == null) {
            return "Cart is empty";
        }
        boolean updated = false;
        for (CartItem ci : cart) {
            if (ci.getProductId() == item.getProductId()) {
                ci.setQuantity(item.getQuantity());
                updated = true;
                break;
            }
        }
        session.setAttribute("cart", cart);
        return updated ? "Cart item updated" : "Item not found in cart";
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
}