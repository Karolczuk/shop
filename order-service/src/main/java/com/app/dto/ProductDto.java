package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String name; // nazwa produktu
    private Long count; // ilosc produktow o danej nazwie


}
