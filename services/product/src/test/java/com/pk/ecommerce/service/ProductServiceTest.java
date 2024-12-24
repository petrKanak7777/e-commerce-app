package com.pk.ecommerce.service;

import com.pk.ecommerce.error.exception.ProductPurchaseException;
import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import com.pk.ecommerce.mapper.ProductMapper;
import com.pk.ecommerce.model.entity.Category;
import com.pk.ecommerce.model.entity.Product;
import com.pk.ecommerce.model.request.ProductPurchaseRequest;
import com.pk.ecommerce.model.request.ProductRequest;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import com.pk.ecommerce.model.response.ProductResponse;
import com.pk.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Integer PRODUCT_ID_0 = 0;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Test
    void test_createProduct_Success() {
        when(productMapper.toProduct(createProductRequest())).thenReturn(createProductEntity());
        when(productRepository.save(any())).thenReturn(createProductEntity());

        Integer productId = productService.createProduct(createProductRequest());

        assertNotNull(productId);
        assertEquals(0, productId);
        verify(productMapper, times(1)).toProduct(createProductRequest());
        verify(productRepository, times(1)).save(createProductEntity());
    }

    @Test
    void test_findByProductId_Success() {
        when(productRepository.findById(PRODUCT_ID_0)).thenReturn(Optional.of(createProductEntity()));
        when(productMapper.toProductResponse(any())).thenReturn(createProductResponse());

        var product = productService.findByProductId(PRODUCT_ID_0);

        assertNotNull(product);
        assertEquals(PRODUCT_ID_0, product.id());
        assertEquals("product0", product.name());
        assertEquals("description0", product.description());
        assertEquals(5.0, product.availableQuantity());
        assertEquals(new BigDecimal(10), product.price());
        assertEquals(0, product.categoryId());
        assertEquals("categoryName", product.categoryName());
        assertEquals("categoryDescription", product.categoryDescription());

        verify(productRepository, times(1)).findById(PRODUCT_ID_0);
        verify(productMapper, times(1)).toProductResponse(any());
    }

    @Test
    void test_findByProductId_NotFound() {
        var productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findByProductId(productId));
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, never()).toProductResponse(any());
    }

    @Test
    void test_findAll_Success() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<ProductResponse> result = productService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(0)).toProductResponse(any());
    }

    @Test
    void test_purchaseProducts_Success() {
        var request = List.of(new ProductPurchaseRequest(0, 2));

        when(productRepository.findAllByIdInOrderById(any())).thenReturn(List.of(createProductEntity()));
        when(productMapper.toProductPurchaseResponse(any(), anyDouble())).thenReturn(createProductPurchaseResponse());

        List<ProductPurchaseResponse> result = productService.purchaseProducts(request);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAllByIdInOrderById(any());
        verify(productRepository, times(1)).save(any());
        verify(productMapper, times(1)).toProductPurchaseResponse(any(), anyDouble());
    }

    @Test
    void test_purchaseProducts_whenStoredProductsDoesNotExist_thenExceptionIsThrown() {
        var request = List.of(new ProductPurchaseRequest(0, 5));

        when(productRepository.findAllByIdInOrderById(any())).thenReturn(List.of());

        assertThrows(ProductPurchaseException.class, () -> productService.purchaseProducts(request));

        verify(productRepository, times(1)).findAllByIdInOrderById(any());
    }

    @Test
    void test_purchaseProducts_whenInsufficientStockQuantityOfProducts_thenExceptionIsThrown() {
        var request = List.of(new ProductPurchaseRequest(0, 5));
        var insufficientStockQuantityOfProducts = List.of(
                new Product(0, "pro0", "desc0", 3.0, new BigDecimal(2), null));

        when(productRepository.findAllByIdInOrderById(any())).thenReturn(insufficientStockQuantityOfProducts);

        assertThrows(ProductPurchaseException.class, () -> productService.purchaseProducts(request));

        verify(productRepository, times(1)).findAllByIdInOrderById(any());
    }

    private ProductRequest createProductRequest() {
        return new ProductRequest(
                "product0",
                "description0",
                1.0,
                new BigDecimal(10),
                0);
    }

    private Product createProductEntity() {
        return Product.builder()
                .id(0)
                .name("product0")
                .description("description0")
                .availableQuantity(5.0)
                .price(new BigDecimal(10))
                .category(
                        Category.builder()
                                .id(0)
                                .name("categoryName")
                                .description("categoryDescription")
                                .build())
                .build();
    }

    private ProductResponse createProductResponse() {
        return new ProductResponse(
                0,
                "product0",
                "description0",
                5.0,
                new BigDecimal(10),
                0,
                "categoryName",
                "categoryDescription"
        );
    }

    private ProductPurchaseResponse createProductPurchaseResponse() {
        return new ProductPurchaseResponse(
                0,
                "product0",
                "description0",
                new BigDecimal(10),
                1.0);
    }
}