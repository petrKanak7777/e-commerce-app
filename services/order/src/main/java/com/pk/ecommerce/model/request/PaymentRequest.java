package com.pk.ecommerce.model.request;

import com.pk.ecommerce.model.PaymentMethod;
import com.pk.ecommerce.model.response.CustomerResponse;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}