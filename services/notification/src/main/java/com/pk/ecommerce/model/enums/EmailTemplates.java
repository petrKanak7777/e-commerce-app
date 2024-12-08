package com.pk.ecommerce.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailTemplates {
    PAYMENT_CONFIRMATION("payment_confirmation.html", "Payment successfully processed"),
    ORDER_CONFIRMATION("order_confirmation", "Order successfully processed");

    @Getter
    private final String template;

    @Getter
    private final String subject;
}