package com.app.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "orders")
public class Order{

    @Id
    @GeneratedValue
    private Long id;

    private String address;
    private LocalDateTime dateTime;
    private Long productId;

//    @NonNull
//    @NotEmpty
//    private List<Long> ids = new ArrayList<>();


}
