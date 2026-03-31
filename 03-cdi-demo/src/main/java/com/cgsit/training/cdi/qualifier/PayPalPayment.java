package com.cgsit.training.cdi.qualifier;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@PayPal
public class PayPalPayment implements PaymentService {

    @Override
    public String pay(double amount) {
        return "Paid %.2f via PayPal".formatted(amount);
    }

    @Override
    public String getName() {
        return "PayPal";
    }
}
