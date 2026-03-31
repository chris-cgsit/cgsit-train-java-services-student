package com.cgsit.training.cdi.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@ApplicationScoped
public class OrderService {

    @Inject
    Logger log;

    @Inject
    Event<OrderCreatedEvent> orderEvent;

    private final AtomicLong idCounter = new AtomicLong();

    public OrderCreatedEvent createOrder(String customer, double total) {
        long id = idCounter.incrementAndGet();
        log.info("Creating order #" + id + " for " + customer);

        var event = new OrderCreatedEvent(id, customer, total);
        orderEvent.fire(event);
        return event;
    }
}
