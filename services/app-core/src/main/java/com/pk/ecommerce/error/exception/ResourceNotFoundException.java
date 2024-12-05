package com.pk.ecommerce.error.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7380094311993645003L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
