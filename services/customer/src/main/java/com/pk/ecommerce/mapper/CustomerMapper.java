package com.pk.ecommerce.mapper;

import com.pk.ecommerce.model.entity.Customer;
import com.pk.ecommerce.model.request.CustomerRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest request) {
        if (Objects.isNull(request)) {
            throw new NullPointerException("Input request value is null");
        }

        return Customer.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .address(request.address())
                .email(request.email())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        if (Objects.isNull(customer)) {
            throw new NullPointerException("Input customer is null");
        }

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress());
    }
}