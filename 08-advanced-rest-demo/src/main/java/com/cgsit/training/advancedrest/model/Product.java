package com.cgsit.training.advancedrest.model;

import java.math.BigDecimal;

public record Product(
    long id,
    String name,
    BigDecimal price,
    String category
) {}
