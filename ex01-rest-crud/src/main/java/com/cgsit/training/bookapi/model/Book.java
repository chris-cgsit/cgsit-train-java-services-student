package com.cgsit.training.bookapi.model;

public record Book(
    long id,
    String title,
    String author,
    String isbn,
    double price
) {}
