package com.app.dto;

import com.app.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDto {

    private String address;
    private LocalDateTime dateTime;
    private Long productId;

//    private List<Long> productIds;

//private List<ProductDto> productDtos

    public Order toOrder() {
        return Order
                .builder()
                .address(address)
                .dateTime(dateTime)
                .build();
    }
}
