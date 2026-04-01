package com.cgsit.training.bookapi.model;

// TODO 1: Importiere die nötigen jakarta.validation.constraints Annotationen
// TODO 2: Importiere die @ValidISBN Custom Constraint (wenn erstellt)

public record Book(
    long id,

    // TODO 3: Füge @NotBlank und @Size(max = 200) hinzu
    String title,

    // TODO 4: Füge @NotBlank hinzu
    String author,

    // TODO 5: Füge @NotBlank und @ValidISBN hinzu
    String isbn,

    // TODO 6: Füge @Positive hinzu
    double price
) {}
