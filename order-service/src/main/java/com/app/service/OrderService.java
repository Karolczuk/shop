package com.app.service;

import com.app.dto.GetProductDto;
import com.app.dto.OrderDto;
import com.app.dto.ProductDto;
import com.app.exception.OrdersServiceException;
import com.app.mapper.Mappers;
import com.app.model.Order;
import com.app.proxy.FindProductProxy;
import com.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Qualifier("orderService")
public class OrderService {

    private final FindProductProxy findProductProxy;
    private final OrderRepository orderRepository;

    public OrderDto createOrder(OrderDto createOrderDto) {
        var productId = createOrderDto.getProductId();
        GetProductDto product = findProductProxy.findById(productId);
        if (product == null) {
            throw new OrdersServiceException("Product id is null");
        }

        var order = createOrderDto
                .toOrder();

        var insertedOrder = orderRepository.save(order);
        if (insertedOrder == null) {
            throw new OrdersServiceException("Cannot insert order");
        }

        return Mappers.fromOrderToOrderDto(insertedOrder);
    }

//    public OrderDto createOrder(List<ProductDto> productDtos, String address) {
//        List<Long> productIds = new ArrayList<>();
//        for (int i = 0; i < productDtos.size(); i++) {
//            GetProductDto product = findProductProxy.findByName(productDtos.get(i).getName());
//            //GetProductDto product = findProductProxy.findById(ids.get(i)); // nazwa produktu i info ile tych produktow chcemy
//            if (product == null) {
//                throw new OrdersServiceException("Product doesn't exists");
//            }
//
//
//            GetProductDto getProductDto = findProductProxy.setProductCount(product, product.getCount());
//
//            productIds.add(getProductDto.getId());
//        }
//
//        Order order = Order.builder()
//                .dateTime(LocalDateTime.now())
//                .ids(productIds)
//                .address(address)
//                .build();
//
//        var insertedOrder = orderRepository.save(order);
//        if (insertedOrder == null) {
//            throw new OrdersServiceException("Cannot insert order");
//        }
//
//        return Mappers.fromOrderToOrderDto(insertedOrder);
//    }


    public List<OrderDto> findOrders() {
        return orderRepository.findAll()
                .stream()
                .map(Mappers::fromOrderToOrderDto)
                .collect(Collectors.toList());
    }

//    public List<OrderDto> findAllOrdersByUser(Long id) {
//        return orderRepository.findOrdersByUser(id)
//                .stream()
//                .map(Mappers::fromOrderToOrderDto)
//                .collect(Collectors.toList());
//    }


}
