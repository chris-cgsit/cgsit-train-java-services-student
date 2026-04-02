package com.cgsit.training.bookapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record Book(
    long id,

    @NotBlank(message = "Titel darf nicht leer sein")
    @Size(max = 200, message = "Titel darf maximal 200 Zeichen lang sein")
    String title,

    @NotBlank(message = "Autor darf nicht leer sein")
    String author,

    @NotBlank(message = "ISBN darf nicht leer sein")
    String isbn,

    @Positive(message = "Preis muss positiv sein")
    double price
) {}
