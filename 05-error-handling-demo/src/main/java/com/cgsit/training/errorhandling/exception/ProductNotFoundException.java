package com.cgsit.training.errorhandling.exception;

public class ProductNotFoundException extends ServiceException {

    public ProductNotFoundException(long id) {
        super("Product with id " + id + " not found", 404);
    }
}
