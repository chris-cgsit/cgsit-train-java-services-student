package com.cgsit.training.cdi.events;

public record OrderCreatedEvent(long orderId, String customer, double total) {
}
