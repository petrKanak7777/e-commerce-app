package com.pk.ecommerce.model.response;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {

}
