package com.example.OrderService;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "orders")
@Data
public class Order {
    @Id
    private Long id;
    private Long productId;
    private Long customerId;
    private Integer quantity;
}