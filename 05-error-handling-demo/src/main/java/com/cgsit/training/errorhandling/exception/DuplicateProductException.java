package com.cgsit.training.errorhandling.exception;

public class DuplicateProductException extends ServiceException {

    public DuplicateProductException(String name) {
        super("Product with name '" + name + "' already exists", 409);
    }
}
