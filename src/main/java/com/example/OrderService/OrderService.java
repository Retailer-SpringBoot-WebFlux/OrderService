package com.example.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    public Mono<Order> createOrder(Order order) {
        return repository.save(order)
                .doOnSuccess(savedOrder -> System.out.println("Order saved with ID: " + savedOrder.getId()));
    }
    public Flux<Order> getAllOrders() {
        return repository.findAll();
    }
}