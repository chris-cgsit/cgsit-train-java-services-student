package com.cgsit.training.cdi.scopes;

import jakarta.enterprise.context.RequestScoped;
import java.util.concurrent.atomic.AtomicInteger;

@RequestScoped
public class RequestCounter {

    private final AtomicInteger count = new AtomicInteger(0);

    public int increment() {
        return count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
