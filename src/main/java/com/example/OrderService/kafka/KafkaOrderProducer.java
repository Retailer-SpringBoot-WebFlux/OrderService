package com.example.OrderService.kafka;

import com.example.OrderService.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KafkaOrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC = "order-topic";

    public Mono<Void> sendOrderEvent(OrderEvent event) {
        return Mono.create(sink ->
                kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                System.out.println("Order Event Sent Successfully: " + event);
                                sink.success();
                            } else {
                                System.err.println("Failed to Send Order Event: " + ex.getMessage());
                                sink.error(ex);
                            }
                        })
        );
    }
}
