package com.cgsit.training.errorhandling.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record Product(
    long id,

    @NotBlank(message = "Name darf nicht leer sein")
    @Size(max = 100, message = "Name darf maximal 100 Zeichen lang sein")
    String name,

    @NotNull(message = "Preis ist erforderlich")
    @Positive(message = "Preis muss positiv sein")
    BigDecimal price,

    @NotBlank(message = "Kategorie darf nicht leer sein")
    String category
) {}
