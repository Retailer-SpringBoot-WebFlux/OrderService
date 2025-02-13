package com.example.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DatabaseClient databaseClient;

    /**
     * Creates a new order after validating that both the customer and product exist.
     */
    public Mono<Order> createOrder(Order order) {
        if (order.getCustomerId() == null || order.getProductId() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID or Product ID cannot be null"));
        }
        order.setId(null);
        if (order.getTotalPrice() == null) {
            order.setTotalPrice(BigDecimal.ZERO);
        }

        return checkCustomerExists(order.getCustomerId())
                .then(checkProductExists(order.getProductId()))
                .then(orderRepository.save(order))
                .doOnSuccess(savedOrder -> System.out.println(" Order saved with ID: " + savedOrder.getId()))
                .doOnError(error -> System.err.println("Error saving order: " + error.getMessage()));
    }

    /**
     * Checks if a customer exists in the database.
     */
    private Mono<Boolean> checkCustomerExists(Long customerId) {
        if (customerId == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID cannot be null"));
        }

        return databaseClient.sql("SELECT id FROM customers WHERE id ="+customerId)

                .map(row -> true)
                .first()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID not found")));
    }


    /**
     * Checks if a product exists in the database.
     */
    private Mono<Boolean> checkProductExists(Long productId) {
        return databaseClient.sql("SELECT id FROM products WHERE id ="+productId)
                .map(row -> true)
                .first()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID not found")));
    }

    /**
     * Retrieves all orders from the database.
     */
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll()
                .doOnError(error -> System.err.println("❌ Error fetching orders: " + error.getMessage()));
    }
}
