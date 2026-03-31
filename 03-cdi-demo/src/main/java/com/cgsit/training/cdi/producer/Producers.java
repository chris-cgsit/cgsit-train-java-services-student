package com.cgsit.training.cdi.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

@ApplicationScoped
public class Producers {

    @Produces
    public Logger createLogger(InjectionPoint ip) {
        return Logger.getLogger(
            ip.getMember().getDeclaringClass().getName()
        );
    }
}
