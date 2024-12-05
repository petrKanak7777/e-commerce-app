package com.pk.ecommerce.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private Integer error;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    ApiError(HttpStatus status, String message, Integer error) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.error = error;
    }
}