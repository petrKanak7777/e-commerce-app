package com.pk.ecommerce.model.response;

import com.pk.ecommerce.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentResponse(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String customerId
) {
}
