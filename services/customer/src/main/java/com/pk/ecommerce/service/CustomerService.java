package com.pk.ecommerce.service;

import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import com.pk.ecommerce.mapper.CustomerMapper;
import com.pk.ecommerce.model.entity.Customer;
import com.pk.ecommerce.model.request.CustomerRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest request) {
        var customer = customerRepository.save(customerMapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        format("Cannot update customer. Customer with id=[%s] not found", request.id())
                ));
        mergerCustomer(customer, request);
        customerRepository.save(customer);
    }

    private void mergerCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (!ObjectUtils.isEmpty(request.address())) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toCustomerResponse)
                .toList();
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId)
                .isPresent();
    }

    public CustomerResponse findByCustomerId(String customerId) {
        if (Objects.isNull(customerId)) {
            throw new NullPointerException("CustomerId value is null");
        }

        return customerRepository.findById(customerId)
                .map(customerMapper::toCustomerResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        format("Cannot get customer. Customer with id=[%s] not found", customerId)
                ));
    }

    public void deleteCustomer(String customerId) {
        if (Objects.isNull(customerId)) {
            throw new NullPointerException("CustomerId value is null");
        }

        customerRepository.deleteById(customerId);
    }
}