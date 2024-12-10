package com.pk.ecommerce.mapper;

import com.pk.ecommerce.model.entity.OrderLine;
import com.pk.ecommerce.model.request.OrderLineRequest;
import com.pk.ecommerce.model.request.PurchaseRequest;
import com.pk.ecommerce.model.response.OrderLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderLineMapper {

    @Mapping(target = "order.id", source = "orderId")
    OrderLine toOrderLine(OrderLineRequest orderLineRequest);

    OrderLineRequest toOrderLineRequest(PurchaseRequest product, Integer orderId);

    @Mapping(target = "orderId", source = "order.id")
    OrderLineResponse toOrderLineResponse(OrderLine orderLine);
}