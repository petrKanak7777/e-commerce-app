package com.pk.ecommerce.model.request;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull(message = "Product Id is required")
        Integer productId,

        @NotNull(message = "Quantity value is required")
        double quantity
) {
}
