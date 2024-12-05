package com.pk.ecommerce.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderLineRequest(
        Integer id,
        Integer orderId,
        @NotNull(message = "Product id is mandatory")
        Integer integer,
        @Positive(message = "Quantity is mandatory")
        double quantity
) {
}