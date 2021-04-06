package com.app.mapper;

import com.app.dto.ProductDto;
import com.app.model.Product;

public interface Mappers {
    static Product fromProductDtoToProduct(ProductDto productDto) {
        return Product
                .builder()
                .name(productDto.getName())
                .discount(productDto.getDiscount())
                .price(productDto.getPrice())
                .count(productDto.getCount())
                .build();
    }

    static ProductDto fromProductProductDto(Product product) {
        return ProductDto
                .builder()
                .id(product.getId())
                .name(product.getName())
                .discount(product.getDiscount())
                .price(product.getPrice())
                .count(product.getCount())
                .build();
    }
}
