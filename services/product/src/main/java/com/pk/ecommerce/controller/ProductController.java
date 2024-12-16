package com.pk.ecommerce.controller;

import com.pk.ecommerce.model.request.ProductPurchaseRequest;
import com.pk.ecommerce.model.request.ProductRequest;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import com.pk.ecommerce.model.response.ProductResponse;
import com.pk.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        log.info("call: /api/v1/products, operation-name: createProduct, params: request=[{}]", request);
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> request
    ) {
        log.info("call: /api/v1/products/purchase, operation-name: purchaseProducts, params: request=[{}]", request);
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findByProductId(
            @PathVariable("product-id") Integer productId
    ) {
        log.info("call: /api/v1/products/{product-id}, operation-name: findByProductId, params: productId=[{}]", productId);
        return ResponseEntity.ok(productService.findByProductId(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        log.info("call: /api/v1/products, operation-name: findAll");
        return ResponseEntity.ok(productService.findAll());
    }
}