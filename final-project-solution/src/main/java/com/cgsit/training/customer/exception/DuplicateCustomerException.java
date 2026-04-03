package com.cgsit.training.customer.exception;

public class DuplicateCustomerException extends ServiceException {

    public DuplicateCustomerException(String email) {
        super("Customer with email '" + email + "' already exists", 409);
    }
}
