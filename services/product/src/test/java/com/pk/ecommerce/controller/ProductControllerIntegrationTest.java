package com.pk.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pk.ecommerce.ProductApplication;
import com.pk.ecommerce.model.entity.Category;
import com.pk.ecommerce.model.entity.Product;
import com.pk.ecommerce.model.request.ProductPurchaseRequest;
import com.pk.ecommerce.model.request.ProductRequest;
import com.pk.ecommerce.repository.CategoryRepository;
import com.pk.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProductApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product testProduct0;
    private Product testProduct1;
    private Product testProduct2;
    private Category testCategory0;
    private Category testCategory1;
    private Category testCategory2;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testCategory0 = Category
                .builder()
                .name("name0")
                .description("desc0")
                .products(List.of())
                .build();

        testCategory1 = Category
                .builder()
                .name("name1")
                .description("desc1")
                .products(List.of())
                .build();

        testCategory2 = Category
                .builder()
                .name("name2")
                .description("desc2")
                .products(List.of())
                .build();

        categoryRepository.save(testCategory0);
        categoryRepository.save(testCategory1);
        categoryRepository.save(testCategory2);

        testProduct0 = Product
                .builder()
                .name("name0")
                .description("desc0")
                .availableQuantity(1.0)
                .price(new BigDecimal(10))
                .category(testCategory0)
                .build();

        testProduct1 = Product
                .builder()
                .name("name1")
                .description("desc1")
                .availableQuantity(2.0)
                .price(new BigDecimal(20))
                .category(testCategory1)
                .build();

        testProduct2 = Product
                .builder()
                .name("name2")
                .description("desc2")
                .availableQuantity(3.0)
                .price(new BigDecimal(25))
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
    void givenProduct_whenFindByProductId_thenSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/products/{productId}", testProduct0.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testProduct0.getName()));
    }

    @Test
    void givenProducts_whenFindAll_thenSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(testProduct0.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(testProduct0.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(testProduct1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(testProduct1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(testProduct2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(testProduct2.getDescription()));
    }

    @Test
    void givenNewProduct_whenCreateProduct_thenSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/products")
                        .content(asJsonString(
                                new ProductRequest("name3", "desc3", 4.0,
                                        new BigDecimal(30), testCategory0.getId())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenProducts_whenPurchaseProducts_thenSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/products/purchase")
                        .content(asJsonString(
                                List.of(new ProductPurchaseRequest(testProduct0.getId(), 1),
                                        new ProductPurchaseRequest(testProduct1.getId(), 2),
                                        new ProductPurchaseRequest(testProduct2.getId(), 2))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(testProduct0.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(testProduct0.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(1.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(testProduct1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(testProduct1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(testProduct2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value(testProduct2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].quantity").value(2.0));
    }

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}