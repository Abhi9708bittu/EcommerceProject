package org.example.ecommerce.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @ElementCollection
    private List<Long> productIds;

    private String paymentMethod;
    private double total;

    public Order() {}
    public Order(String username, List<Long> productIds, String paymentMethod, double total) {
        this.username = username;
        this.productIds = productIds;
        this.paymentMethod = paymentMethod;
        this.total = total;
    }
    // Getters and setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}