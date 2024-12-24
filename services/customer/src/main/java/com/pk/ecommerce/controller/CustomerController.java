package com.pk.ecommerce.controller;

import com.pk.ecommerce.model.request.CustomerRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ) {
        log.info("call: /api/v1/customers, operation-name: createCustomer, params: request=[{}]", request);
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(
            @RequestBody @Valid CustomerRequest request
    ) {
        log.info("call: /api/v1/customers, operation-name: updateCustomer, params: request=[{}]", request);
        customerService.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        log.info("call: /api/v1/customers, operation-name: findAll, params:-");
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("customer-id") String customerId
    ) {
        log.info("call: /api/v1/customers/exists/{customer-id}, operation-name: existsById, params: customer-id=[{}]", customerId);
        return ResponseEntity.ok(customerService.existsById(customerId));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findByCustomerId(
            @PathVariable("customer-id") String customerId
    ) {
        log.info("call: /api/v1/customers/{customer-id}, operation-name: findByCustomerId, params: customer-id=[{}]", customerId);
        return ResponseEntity.ok(customerService.findByCustomerId(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable("customer-id") String customerId
    ) {
        log.info("call: /api/v1/customers/{customer-id}, operation-name: deleteCustomer, params: customer-id=[{}]", customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }

}