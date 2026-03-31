package com.cgsit.training.cdi.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailObserver {

    @Inject
    Logger log;

    public void onOrderCreated(@Observes OrderCreatedEvent event) {
        log.info("[EMAIL] Sending confirmation to " + event.customer()
                + " for order #" + event.orderId()
                + " (total: " + event.total() + ")");
    }
}
