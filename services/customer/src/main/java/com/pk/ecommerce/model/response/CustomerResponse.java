package com.pk.ecommerce.model.response;

import com.pk.ecommerce.model.entity.Address;
import lombok.Builder;

@Builder
public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Address address) {
}
