package com.pk.ecommerce.error.Exception;

import java.io.Serial;

public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6351751936556877104L;

    public BusinessException(String message) {
        super(message);
    }
}