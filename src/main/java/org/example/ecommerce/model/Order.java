package org.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @ElementCollection
    private List<Long> productIds;

    private String paymentMethod;
    private double total;

    public Order(String username, List<Long> productIds, String paymentMethod, double total) {
        this.username = username;
        this.productIds = productIds;
        this.paymentMethod = paymentMethod;
        this.total = total;
    }
}