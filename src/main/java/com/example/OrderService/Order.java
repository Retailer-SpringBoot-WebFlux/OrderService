package com.example.OrderService;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("orders")
@Data
public class Order {

    @Id
    private Long id;
    private Long productId;
    private Long customerId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String orderStatus;
    private LocalDateTime orderDate;
}
