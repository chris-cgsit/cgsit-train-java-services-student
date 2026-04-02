package com.cgsit.training.testing.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record Product(
        Long id,

        @NotBlank(message = "Name darf nicht leer sein")
        @Size(min = 2, max = 100, message = "Name muss zwischen 2 und 100 Zeichen lang sein")
        String name,

        @Positive(message = "Preis muss positiv sein")
        double price
) {
    public Product withId(Long id) {
        return new Product(id, this.name, this.price);
    }
}
