package com.example.OrderService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "id", target = "orderId") // Mapping `id` from Order to `orderId` in OrderEvent
    OrderEvent toOrderEvent(Order order);
}
