package com.app.controller;

import com.app.dto.OrderDto;
import com.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{address}")
    public OrderDto createProduct(@RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @GetMapping
    public List<OrderDto> findAllOrders() {
        return orderService.findOrders();
    }

}
