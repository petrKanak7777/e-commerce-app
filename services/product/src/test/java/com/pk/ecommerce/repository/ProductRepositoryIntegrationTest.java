package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Category;
import com.pk.ecommerce.model.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Product testProduct0;
    private Product testProduct1;
    private Product testProduct2;
    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        var testCategory0 = Category
                .builder()
                .name("name0")
                .description("desc0")
                .products(List.of())
                .build();

        var testCategory1 = Category
                .builder()
                .name("name1")
                .description("desc1")
                .products(List.of())
                .build();

        var testCategory2 = Category
                .builder()
                .name("name2")
                .description("desc2")
                .products(List.of())
                .build();

        testEntityManager.persist(testCategory0);
        testEntityManager.persist(testCategory1);
        testEntityManager.persist(testCategory2);

        testProduct0 = Product
                .builder()
                .name("name0")
                .description("desc0")
                .availableQuantity(2.0)
                .price(new BigDecimal(10))
                .category(testCategory0)
                .build();

        testProduct1 = Product
                .builder()
                .name("name1")
                .description("desc1")
                .availableQuantity(3.0)
                .price(new BigDecimal(5))
                .category(testCategory1)
                .build();

        testProduct2 = Product
                .builder()
                .name("name2")
                .description("desc2")
                .availableQuantity(7.0)
                .price(new BigDecimal(8))
                .category(testCategory2)
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
    void givenProduct_whenSave_thenSuccess() {
        var insertedProduct = Product
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

        var savedProduct = productRepository.save(insertedProduct);

        assertNotNull(savedProduct);
        assertEquals(insertedProduct.getName(), savedProduct.getName());
        assertEquals(insertedProduct.getDescription(), savedProduct.getDescription());
        assertThat(entityManager.find(Product.class, insertedProduct.getId())).isEqualTo(savedProduct);
    }

    @Test
    void givenProductCreated_whenFindById_thenSuccess() {
        var savedProduct = productRepository.findById(testProduct0.getId()).orElse(null);

        assertNotNull(savedProduct);
        assertEquals(testProduct0.getId(), savedProduct.getId());
        assertEquals(testProduct0.getName(), savedProduct.getName());
    }

    @Test
    void givenProductsCreated_whenFindAll_thenSuccess() {
        var savedProducts = productRepository.findAll();

        assertNotNull(savedProducts);
        assertEquals(3, savedProducts.size());
        assertEquals(testProduct0.getName(), savedProducts.get(0).getName());
        assertEquals(testProduct0.getDescription(), savedProducts.get(0).getDescription());
        assertEquals(testProduct1.getName(), savedProducts.get(1).getName());
        assertEquals(testProduct1.getDescription(), savedProducts.get(1).getDescription());
        assertEquals(testProduct2.getName(), savedProducts.get(2).getName());
        assertEquals(testProduct2.getDescription(), savedProducts.get(2).getDescription());
    }

    @Test
    void givenProductsCreated_whenFindAllByIdInOrderById_thenSuccess() {
        var productIds = productRepository.findAll()
                .stream()
                .map(Product::getId)
                .toList();

        var savedProducts = productRepository.findAllByIdInOrderById(productIds);

        assertNotNull(savedProducts);
        assertEquals(3, savedProducts.size());
        assertEquals(testProduct0.getId(), savedProducts.getFirst().getId());
        assertEquals(testProduct0.getName(), savedProducts.getFirst().getName());
        assertEquals(testProduct0.getDescription(), savedProducts.getFirst().getDescription());
        assertEquals(testProduct1.getId(), savedProducts.get(1).getId());
        assertEquals(testProduct1.getName(), savedProducts.get(1).getName());
        assertEquals(testProduct1.getDescription(), savedProducts.get(1).getDescription());
        assertEquals(testProduct2.getId(), savedProducts.get(2).getId());
        assertEquals(testProduct2.getName(), savedProducts.get(2).getName());
        assertEquals(testProduct2.getDescription(), savedProducts.get(2).getDescription());
    }
}