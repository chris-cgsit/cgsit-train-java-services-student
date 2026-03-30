package com.cgsit.training.servicelayer.model;

/**
 * Product record — immutable data carrier.
 * Used by all layers (resource, service, repository).
 * JSON-B serializes this automatically.
 */
public record Product(
    long id,
    String name,
    double price,
    String category
) {}
