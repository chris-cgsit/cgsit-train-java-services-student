package com.cgsit.training.customer.exception;

public class CustomerNotFoundException extends ServiceException {

    public CustomerNotFoundException(Long id) {
        super("Customer with id " + id + " not found", 404);
    }
}
