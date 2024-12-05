package com.pk.ecommerce.model.response;

public record OrderLineResponse(
        Integer id,
        Integer productId,
        double quantity,
        Integer orderId
) {
}