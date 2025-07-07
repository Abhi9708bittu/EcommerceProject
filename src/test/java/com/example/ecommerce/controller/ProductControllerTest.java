package com.example.ecommerce.controller;

import org.example.ecommerce.controller.ProductController;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {
    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    void setup() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    void test_get_all_products() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Desc 1", 10.0),
                new Product(2L, "Product 2", "Desc 2", 20.0)
        );
        when(productService.getAllProducts()).thenReturn(products);
        List<Product> result = productController.getAllProducts();
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
    }

    @Test
    void test_get_product_by_id_found() {
        Product product = new Product(1L, "Product 1", "Desc 1", 10.0);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        Product result = productController.getProduct(1L);
        assertNotNull(result);
        assertEquals("Product 1", result.getName());
    }

    @Test
    void test_get_product_by_id_not_found() {
        when(productService.getProductById(2L)).thenReturn(Optional.empty());
        Product result = productController.getProduct(2L);
        assertNull(result);
    }

    @Test
    void test_add_product() {
        Product product = new Product(null, "Product 3", "Desc 3", 30.0);
        Product savedProduct = new Product(3L, "Product 3", "Desc 3", 30.0);
        when(productService.addProduct(product)).thenReturn(savedProduct);
        Product result = productController.addProduct(product);
        assertEquals(3L, result.getId());
        assertEquals("Product 3", result.getName());
    }
} 