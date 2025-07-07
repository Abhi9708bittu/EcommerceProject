package com.example.ecommerce.service;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Test
    void test_get_all_products() {
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

    @Test
    void test_get_product_by_id_found() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductService(mockRepo);
        Product product = new Product(1L, "Product 1", "Desc 1", 10.0);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductById(1L);
        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().getName());
    }

    @Test
    void test_get_product_by_id_not_found() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductService(mockRepo);
        when(mockRepo.findById(2L)).thenReturn(Optional.empty());
        Optional<Product> result = productService.getProductById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void test_add_product() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductService(mockRepo);
        Product product = new Product(null, "Product 3", "Desc 3", 30.0);
        Product savedProduct = new Product(3L, "Product 3", "Desc 3", 30.0);
        when(mockRepo.save(product)).thenReturn(savedProduct);
        Product result = productService.addProduct(product);
        assertEquals(3L, result.getId());
        assertEquals("Product 3", result.getName());
    }
}