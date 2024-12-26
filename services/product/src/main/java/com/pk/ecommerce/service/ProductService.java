package com.pk.ecommerce.service;

import com.pk.ecommerce.error.exception.ProductPurchaseException;
import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import com.pk.ecommerce.mapper.ProductMapper;
import com.pk.ecommerce.metric.ProductMetrics;
import com.pk.ecommerce.model.entity.Product;
import com.pk.ecommerce.model.request.ProductPurchaseRequest;
import com.pk.ecommerce.model.request.ProductRequest;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import com.pk.ecommerce.model.response.ProductResponse;
import com.pk.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductMetrics productMetrics;

    public Integer createProduct(ProductRequest request) {
        var product = productRepository.save(productMapper.toProduct(request));
        log.info("INFO - Product with id=[{}] was successfully created", product.getId());
        return product.getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productsId = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        var storedProducts = productRepository.findAllByIdInOrderById(productsId);
        if(productsId.size() != storedProducts.size()) {
            throw new ProductPurchaseException("ERROR - One or more products does not exists");
        }

        var storedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        var availableProducts = new ArrayList<Product>();
        computePurchasedAndAvailableProducts(
                storedRequest, storedProducts, availableProducts, purchasedProducts);

        productRepository.saveAll(availableProducts);

        log.info("INFO - Products was successfully purchased");
        productMetrics.incApiCallPurchasedProduct();
        return purchasedProducts;
    }

    private void computePurchasedAndAvailableProducts(
            List<ProductPurchaseRequest> storedRequest,
            List<Product> storedProducts,
            List<Product> availableProducts,
            List<ProductPurchaseResponse> purchasedProducts) {
        for(int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);
            if(product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                        String.format("ERROR - Insufficient stock quantity for product with id=[%s]. Requested quantity=[%,.4f], but available quantity=[%,.4f]",
                                productRequest.productId(),
                                productRequest.quantity(),
                                product.getAvailableQuantity()));
            }

            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);

            availableProducts.add(product);
            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
    }

    public ProductResponse findByProductId(Integer productId) {
        var product = productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        format("ERROR - Cannot get product. Product with id=[%s] not found", productId)
                ));
        log.info("INFO - Product with id=[{}] was found", product.id());
        return product;
    }

    public List<ProductResponse> findAll() {
        var products = productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        log.info("INFO - Products with size=[{}] was successfully returned", products.size());
        return products;
    }
}