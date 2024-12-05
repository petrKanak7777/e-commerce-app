package com.pk.ecommerce.service;

import com.pk.ecommerce.mapper.OrderLineMapper;
import com.pk.ecommerce.model.entity.OrderLine;
import com.pk.ecommerce.model.request.OrderLineRequest;
import com.pk.ecommerce.model.response.OrderLineResponse;
import com.pk.ecommerce.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        return orderLineRepository.save(orderLineMapper.toOrderLine(orderLineRequest)).getId();
    }

    public List<Integer> saveOrderLines(List<OrderLineRequest> orderLineRequests) {
        var orderLines = orderLineRequests
                .stream()
                .map(orderLineMapper::toOrderLine)
                .toList();

        return orderLineRepository.saveAll(orderLines)
                .stream()
                .map(OrderLine::getId)
                .toList();
    }

    public List<OrderLineResponse> findByOrderId(Integer orderId) {
        var orderLines = orderLineRepository.findAllByOrderId(orderId);
        return orderLines
                .stream()
                .map(orderLineMapper::toOrderLineResponse)
                .toList();
    }
}
