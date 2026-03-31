package com.cgsit.training.datatypes.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Alle Java-Typen auf einen Blick — zeigt wie jeder Typ in JSON serialisiert wird.
 */
@Schema(description = "Showcase aller gängigen Java ↔ JSON Typmappings")
public record TypeShowcase(

    // --- Primitive & Wrapper ---
    @Schema(description = "byte → JSON number", example = "127")
    byte byteValue,

    @Schema(description = "short → JSON number", example = "32000")
    short shortValue,

    @Schema(description = "int → JSON number", example = "42")
    int intValue,

    @Schema(description = "long → JSON number", example = "9000000001")
    long longValue,

    @Schema(description = "float → JSON number (Achtung: Rundungsfehler!)", example = "3.14")
    float floatValue,

    @Schema(description = "double → JSON number (Achtung: Rundungsfehler!)", example = "3.141592653589793")
    double doubleValue,

    @Schema(description = "boolean → JSON true/false", example = "true")
    boolean booleanValue,

    @Schema(description = "char → JSON string (1 Zeichen)", example = "A")
    char charValue,

    // --- Exakte Zahlen ---
    @Schema(description = "BigDecimal → JSON number (exakt, für Geld!)", example = "19999.99")
    BigDecimal bigDecimalValue,

    @Schema(description = "BigInteger → JSON number (beliebig groß)", example = "123456789012345678901234567890")
    BigInteger bigIntegerValue,

    // --- Text ---
    @Schema(description = "String → JSON string", example = "Hallo Welt")
    String stringValue,

    @Schema(description = "UUID → JSON string", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID uuidValue,

    // --- Datum & Zeit (java.time) ---
    @Schema(description = "LocalDate → JSON \"2026-03-31\"", example = "2026-03-31")
    LocalDate localDate,

    @Schema(description = "LocalTime → JSON \"14:30:00\"", example = "14:30:00")
    LocalTime localTime,

    @Schema(description = "LocalDateTime → JSON \"2026-03-31T14:30:00\"", example = "2026-03-31T14:30:00")
    LocalDateTime localDateTime,

    @Schema(description = "OffsetDateTime → JSON \"2026-03-31T14:30:00+02:00\" (mit Zeitzone)", example = "2026-03-31T14:30:00+02:00")
    OffsetDateTime offsetDateTime,

    @Schema(description = "Instant → JSON \"2026-03-31T12:30:00Z\" (UTC Timestamp)", example = "2026-03-31T12:30:00Z")
    Instant instant,

    @Schema(description = "Duration → JSON \"PT1H30M\" (ISO 8601 Dauer)", example = "PT1H30M")
    Duration duration,

    // --- Collections ---
    @Schema(description = "List<String> → JSON Array [\"a\",\"b\"]")
    List<String> stringList,

    @Schema(description = "List<Integer> → JSON Array [1,2,3]")
    List<Integer> intList,

    @Schema(description = "Map<String,String> → JSON Object {\"key\":\"value\"}")
    Map<String, String> stringMap,

    @Schema(description = "Map<String,Object> → JSON Object (gemischte Typen)")
    Map<String, Object> mixedMap,

    // --- Nullable ---
    @Schema(description = "Nullable String → JSON null oder \"text\"", nullable = true)
    String nullableValue

) {}
