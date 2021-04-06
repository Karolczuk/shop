package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private BigDecimal price;

    private double discount;

    private Long count;


   // private Order order;

}
