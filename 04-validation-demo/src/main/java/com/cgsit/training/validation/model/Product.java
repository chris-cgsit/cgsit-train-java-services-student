package com.cgsit.training.validation.model;

import com.cgsit.training.validation.constraint.ValidCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Product record with Jackson serialization + Bean Validation + OpenAPI annotations.
 *
 * Jackson annotations (provided by WildFly 39 via RESTEasy Jackson Provider):
 *   @JsonPropertyOrder  — field order in JSON output
 *   @JsonProperty       — rename field, read-only/write-only access
 *   @JsonInclude        — omit null fields
 *   @JsonFormat         — date/time/number formatting
 *   @JsonIgnore         — exclude field from JSON entirely
 */
@JsonPropertyOrder({"id", "name", "price", "category", "availableFrom", "availableUntil", "weight"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Product with Bean Validation + Jackson serialization")
public record Product(

    // @JsonProperty(access = READ_ONLY): field appears in responses but is
    // ignored when sent in requests (server generates the ID).
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Product ID (auto-generated)", example = "1", readOnly = true)
    long id,

    @NotBlank(message = "Name darf nicht leer sein")
    @Size(max = 100, message = "Name darf maximal 100 Zeichen lang sein")
    @Schema(description = "Product name", example = "Mechanical Keyboard", maxLength = 100)
    String name,

    // @JsonFormat(shape = STRING): BigDecimal is serialized as string "89.99"
    // instead of number 89.99 — avoids floating point precision issues.
    @NotNull(message = "Preis ist erforderlich")
    @Positive(message = "Preis muss positiv sein")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Price as string (avoids floating point issues)", example = "89.99")
    BigDecimal price,

    @NotBlank(message = "Kategorie darf nicht leer sein")
    @ValidCategory
    @Schema(description = "Category (Electronics, Peripherie, Software, Books)",
            example = "Peripherie",
            enumeration = {"Electronics", "Peripherie", "Software", "Books"})
    String category,

    // @JsonFormat(pattern): controls the date format in JSON.
    // Default would be ISO-8601 "2026-04-01", pattern makes it explicit.
    @NotNull(message = "Verfügbarkeitsdatum ist erforderlich")
    @FutureOrPresent(message = "Verfügbarkeitsdatum darf nicht in der Vergangenheit liegen")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Available from date (ISO-8601)", example = "2026-04-01")
    LocalDate availableFrom,

    // LocalTime: "09:00" or "09:00:00" in JSON.
    // @JsonFormat(pattern) controls the precision shown.
    @NotNull(message = "Verkaufszeit-Start ist erforderlich")
    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "Daily sales start time", example = "09:00")
    LocalTime availableUntil,

    // @JsonIgnore: field exists in Java but never appears in JSON.
    // Useful for internal data (audit, cache, computed values).
    @JsonIgnore
    @Schema(hidden = true)
    @Positive(message = "Gewicht muss positiv sein")
    BigDecimal weight
) {}
