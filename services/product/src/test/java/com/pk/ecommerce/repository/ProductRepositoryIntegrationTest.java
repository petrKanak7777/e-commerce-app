package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Category;
import com.pk.ecommerce.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

// https://www.baeldung.com/spring-boot-testcontainers-integration-test
// https://www.baeldung.com/hibernate-unsaved-transient-instance-error - Product -> ManyToOne -> cascade = CascadeType.ALL
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    private Product testProduct0;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        testProduct0 = Product
                .builder()
                .name("name0")
                .description("desc0")
                .availableQuantity(2.0)
                .price(new BigDecimal(10))
                .category(Category
                        .builder()
                        .name("name0")
                        .description("desc0")
                        .products(List.of())
                        .build())
                .build();

        testProduct1 = Product
                .builder()
                .name("name1")
                .description("desc1")
                .availableQuantity(3.0)
                .price(new BigDecimal(5))
                .category(Category
                        .builder()
                        .name("name1")
                        .description("desc1")
                        .products(List.of())
                        .build())
                .build();

        testProduct2 = Product
                .builder()
                .name("name2")
                .description("desc2")
                .availableQuantity(7.0)
                .price(new BigDecimal(8))
                .category(Category
                        .builder()
                        .name("name2")
                        .description("desc2")
                        .products(List.of())
                        .build())
                .build();

        productRepository.save(testProduct0);
        productRepository.save(testProduct1);
        productRepository.save(testProduct2);
    }

    @AfterEach
    void clean() {
        productRepository.delete(testProduct0);
        productRepository.delete(testProduct1);
        productRepository.delete(testProduct2);
    }

    @Test
    void testSave_givenProduct_whenSaved_thenSuccess() {
        var productToSave = Product
                .builder()
                .name("name")
                .description("desc")
                .availableQuantity(2.0)
                .price(new BigDecimal(10))
                .category(Category
                        .builder()
                        .name("name")
                        .description("desc")
                        .products(List.of())
                        .build())
                .build();

        var savedProduct = productRepository.save(productToSave);

        assertNotNull(savedProduct);
        assertEquals(productToSave.getId(), savedProduct.getId());
        assertEquals(productToSave.getName(), savedProduct.getName());
    }

    @Test
    void testFindById_givenProduct_whenSaved_thenCanBeFoundById() {
        var savedProduct = productRepository.findById(testProduct0.getId()).orElse(null);

        assertNotNull(savedProduct);
        assertEquals(testProduct0.getId(), savedProduct.getId());
        assertEquals(testProduct0.getName(), savedProduct.getName());
    }

    @Test
    void testFindAll_givenProducts_whenFindAll_thenSuccess() {
        var savedProducts = productRepository.findAll();

        assertNotNull(savedProducts);
        assertEquals(3, savedProducts.size());
        assertEquals(savedProducts.get(0).getName(), "name0");
        assertEquals(savedProducts.get(0).getDescription(), "desc0");
        assertEquals(savedProducts.get(1).getName(), "name1");
        assertEquals(savedProducts.get(1).getDescription(), "desc1");
        assertEquals(savedProducts.get(2).getName(), "name2");
        assertEquals(savedProducts.get(2).getDescription(), "desc2");
    }

    @Test
    void testFindAllByIdInOrderById_givenProducts_whenFindAllByIdInOrderById_thenSuccess() {
        var productIds = List.of(1, 2, 3);

        var savedProducts = productRepository.findAllByIdInOrderById(productIds);

        assertNotNull(savedProducts);
        assertEquals(3, savedProducts.size());
        assertEquals(savedProducts.get(0).getId(), 1);
        assertEquals(savedProducts.get(0).getName(), "name0");
        assertEquals(savedProducts.get(0).getDescription(), "desc0");
        assertEquals(savedProducts.get(1).getId(), 2);
        assertEquals(savedProducts.get(1).getName(), "name1");
        assertEquals(savedProducts.get(1).getDescription(), "desc1");
        assertEquals(savedProducts.get(2).getId(), 3);
        assertEquals(savedProducts.get(2).getName(), "name2");
        assertEquals(savedProducts.get(2).getDescription(), "desc2");
    }
}