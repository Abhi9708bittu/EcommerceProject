package com.example.ecommerce.service;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Test
    void shouldReturnAllProducts() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductService(mockRepo);

        List<Product> mockProducts = Arrays.asList(
                new Product(1L, "Product 1", "Desc 1", 10.0),
                new Product(2L, "Product 2", "Desc 2", 20.0)
        );
        when(mockRepo.findAll()).thenReturn(mockProducts);

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
    }
}