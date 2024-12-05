package com.pk.ecommerce.error.exception;

import java.io.Serial;

public class InvalidArgumentException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5568539747246362792L;

    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String s) {
        super(s);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }
}
