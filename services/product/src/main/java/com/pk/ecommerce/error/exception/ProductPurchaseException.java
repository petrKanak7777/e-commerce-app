package com.pk.ecommerce.error.exception;

import java.io.Serial;

public class ProductPurchaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3187014432984194364L;

    public ProductPurchaseException(String message) {
        super(message);
    }
}
