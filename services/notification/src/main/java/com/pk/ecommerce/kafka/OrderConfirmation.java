package com.pk.ecommerce.kafka;

import com.pk.ecommerce.model.enums.PaymentMethod;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customerResponse,
        List<ProductPurchaseResponse> products
) {
}