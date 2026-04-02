package com.cgsit.training.testing.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Produkt mit ID " + id + " nicht gefunden");
    }
}
