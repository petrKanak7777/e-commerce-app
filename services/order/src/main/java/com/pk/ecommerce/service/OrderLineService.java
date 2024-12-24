package com.pk.ecommerce.service;

import com.pk.ecommerce.mapper.OrderLineMapper;
import com.pk.ecommerce.model.entity.OrderLine;
import com.pk.ecommerce.model.request.OrderLineRequest;
import com.pk.ecommerce.model.request.PurchaseRequest;
import com.pk.ecommerce.model.response.OrderLineResponse;
import com.pk.ecommerce.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    public List<Integer> saveOrderLines(List<PurchaseRequest> products, Integer orderId) {
        var orderLines = products
                .stream()
                .map(p -> orderLineMapper.toOrderLineRequest(p, orderId))
                .map(orderLineMapper::toOrderLine)
                .toList();

        var orderLinesId = orderLineRepository.saveAll(orderLines)
                .stream()
                .map(OrderLine::getId)
                .toList();
        log.info("INFO - Order lines=[{}] was successfully saved", orderLinesId);
        return orderLinesId;
    }

    public List<OrderLineResponse> findByOrderId(Integer orderId) {
        var orderLines = orderLineRepository.findAllByOrderId(orderId);
        log.info("INFO - orderLines with size=[{}] was successfully returned", orderLines.size());
        return orderLines
                .stream()
                .map(orderLineMapper::toOrderLineResponse)
                .toList();
    }
}