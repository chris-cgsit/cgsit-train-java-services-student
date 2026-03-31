package com.cgsit.training.rest.model;

public record Product(
    long id,
    String name,
    double price,
    String category
) {}
