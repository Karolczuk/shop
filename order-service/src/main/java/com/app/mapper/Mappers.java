package com.app.mapper;

import com.app.dto.OrderDto;
import com.app.model.Order;

import java.util.stream.Collectors;

public interface Mappers {

    static Order fromOrderDtoToOrder(OrderDto orderDto) {
        return Order
                .builder()
              //  .ids(orderDto.getProductIds())
                .productId(orderDto.getProductId())
                .address(orderDto.getAddress())
                .dateTime(orderDto.getDateTime())
                .build();
    }

    static OrderDto fromOrderToOrderDto(Order order) {
        return OrderDto
                .builder()
                ///.productIds(order.getIds())
                .productId(order.getProductId())
                .address(order.getAddress())
                .dateTime(order.getDateTime())
                .build();
    }

}
