package com.cgsit.training.cdi.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class AuditObserver {

    @Inject
    Logger log;

    public void onOrderCreated(@Observes OrderCreatedEvent event) {
        log.info("[AUDIT] Order #" + event.orderId()
                + " created by " + event.customer()
                + " — total: " + event.total());
    }
}
