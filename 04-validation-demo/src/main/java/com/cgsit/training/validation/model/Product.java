package com.cgsit.training.validation.model;

import com.cgsit.training.validation.constraint.ValidCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Product with Bean Validation constraints")
public record Product(

    @Schema(description = "Product ID (auto-generated)", example = "1", readOnly = true)
    long id,

    @NotBlank(message = "Name darf nicht leer sein")
    @Size(max = 100, message = "Name darf maximal 100 Zeichen lang sein")
    @Schema(description = "Product name", example = "Mechanical Keyboard", maxLength = 100)
    String name,

    @NotNull(message = "Preis ist erforderlich")
    @Positive(message = "Preis muss positiv sein")
    @Schema(description = "Price (must be positive)", example = "89.99")
    BigDecimal price,

    @NotBlank(message = "Kategorie darf nicht leer sein")
    @ValidCategory
    @Schema(description = "Category (Electronics, Peripherie, Software, Books)",
            example = "Peripherie",
            enumeration = {"Electronics", "Peripherie", "Software", "Books"})
    String category
) {}
