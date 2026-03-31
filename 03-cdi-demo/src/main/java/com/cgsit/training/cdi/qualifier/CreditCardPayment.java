package com.cgsit.training.cdi.qualifier;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@CreditCard
public class CreditCardPayment implements PaymentService {

    @Override
    public String pay(double amount) {
        return "Paid %.2f via Credit Card".formatted(amount);
    }

    @Override
    public String getName() {
        return "Credit Card";
    }
}
