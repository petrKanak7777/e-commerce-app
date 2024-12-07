package com.pk.ecommerce.controller;

import com.pk.ecommerce.model.request.OrderRequest;
import com.pk.ecommerce.model.response.OrderResponse;
import com.pk.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @RequestBody @Valid OrderRequest request
    ) {
        log.info("call: /api/v1/orders, operation-name: createOrder, params: request=[{}]", request);
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> findAll() {
        log.info("call: /api/v1/orders, operation-name: findAll");
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findByOrderId(
            @PathVariable("order-id") Integer orderId
    ) {
        log.info("call: /api/v1/orders/{order-id}, operation-name: findByOrderId, params: order-id=[{}]", orderId);
        return ResponseEntity.ok(orderService.findByOrderId(orderId));
    }
}
