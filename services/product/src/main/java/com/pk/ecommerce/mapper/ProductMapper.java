package com.pk.ecommerce.mapper;

import com.pk.ecommerce.model.entity.Category;
import com.pk.ecommerce.model.entity.Product;
import com.pk.ecommerce.model.request.ProductRequest;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import com.pk.ecommerce.model.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        if (Objects.isNull(request)) {
            throw new NullPointerException("Input request value is null");
        }

        return Product.builder()
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(
                        Category
                                .builder()
                                .id(request.categoryId()).build())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        if (Objects.isNull(product)) {
            throw new NullPointerException("Input product is null");
        }

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription());
    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}