package com.pk.ecommerce.controller;

import com.pk.ecommerce.model.response.OrderLineResponse;
import com.pk.ecommerce.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findByOrderId(
            @PathVariable("order-id") Integer orderId
    ) {
        log.info("call: /api/v1/order-lines/order/{order-id}, operation-name: findByOrderId, params: order-id=[{}]", orderId);
        return ResponseEntity.ok(orderLineService.findByOrderId(orderId));
    }
}