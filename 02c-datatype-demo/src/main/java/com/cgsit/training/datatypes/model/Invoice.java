package com.cgsit.training.datatypes.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Demonstrates all common Java ↔ JSON ↔ OpenAPI type mappings.
 *
 * ┌──────────────────────┬───────────────────────┬──────────────────────────────┐
 * │ Java Type            │ JSON Type             │ OpenAPI Type                 │
 * ├──────────────────────┼───────────────────────┼──────────────────────────────┤
 * │ String               │ string                │ type: string                 │
 * │ int / Integer         │ number                │ type: integer, format: int32 │
 * │ long / Long           │ number                │ type: integer, format: int64 │
 * │ double / Double       │ number                │ type: number, format: double │
 * │ float / Float         │ number                │ type: number, format: float  │
 * │ BigDecimal           │ number                │ type: number (arbitrary prec)│
 * │ boolean / Boolean     │ true/false            │ type: boolean                │
 * │ LocalDate            │ "2026-03-31"          │ type: string, format: date   │
 * │ LocalTime            │ "14:30:00"            │ type: string, format: time   │
 * │ LocalDateTime        │ "2026-03-31T14:30:00" │ type: string, format: date-time│
 * │ List<T>              │ [ ... ]               │ type: array, items: ...      │
 * │ Map<String,T>        │ { "key": ... }        │ type: object, additionalProp │
 * │ enum                 │ "VALUE"               │ type: string, enum: [...]    │
 * │ null                 │ null                  │ nullable: true               │
 * └──────────────────────┴───────────────────────┴──────────────────────────────┘
 */
@Schema(description = "Rechnung — demonstriert alle gängigen Datentypen")
public record Invoice(

    // --- Ganzzahlen ---
    @Schema(description = "Rechnungsnummer", example = "1001")
    int id,

    @Schema(description = "Kundennummer (long für große IDs)", example = "9000000001")
    long customerId,

    // --- Text ---
    @Schema(description = "Kundenname", example = "Max Mustermann")
    String customerName,

    @Schema(description = "E-Mail Adresse", example = "max@example.com")
    String email,

    // --- Dezimalzahlen ---
    @Schema(description = "Nettobetrag (double — NICHT für Geld empfohlen!)", example = "199.99")
    double netAmountDouble,

    @Schema(description = "Nettobetrag (BigDecimal — RICHTIG für Geldbeträge)", example = "199.99")
    BigDecimal netAmount,

    @Schema(description = "Steuersatz in Prozent", example = "20.0")
    BigDecimal taxRate,

    @Schema(description = "Bruttobetrag", example = "239.99")
    BigDecimal grossAmount,

    // --- Boolean ---
    @Schema(description = "Rechnung bezahlt?", example = "false")
    boolean paid,

    // --- Datum & Zeit ---
    @Schema(description = "Rechnungsdatum (nur Datum)", example = "2026-03-31")
    LocalDate invoiceDate,

    @Schema(description = "Fälligkeitsdatum", example = "2026-04-30")
    LocalDate dueDate,

    @Schema(description = "Erstellungszeit (nur Uhrzeit)", example = "14:30:00")
    LocalTime createdTime,

    @Schema(description = "Letzte Änderung (Datum + Uhrzeit)", example = "2026-03-31T14:30:00")
    LocalDateTime lastModified,

    // --- Enum ---
    @Schema(description = "Rechnungsstatus", example = "OPEN")
    InvoiceStatus status,

    // --- Collections ---
    @Schema(description = "Rechnungspositionen (Liste)")
    List<LineItem> items,

    @Schema(description = "Metadaten (Key-Value Paare)")
    Map<String, String> metadata

) {

    /**
     * Enum — wird zu JSON String + OpenAPI enum
     */
    @Schema(description = "Mögliche Rechnungsstatus")
    public enum InvoiceStatus {
        DRAFT,
        OPEN,
        PAID,
        OVERDUE,
        CANCELLED
    }

    /**
     * Nested Record — wird zu JSON Object + OpenAPI $ref
     */
    @Schema(description = "Einzelne Rechnungsposition")
    public record LineItem(
        @Schema(description = "Positionsnummer", example = "1")
        int position,

        @Schema(description = "Artikelbezeichnung", example = "Laptop")
        String description,

        @Schema(description = "Anzahl", example = "2")
        int quantity,

        @Schema(description = "Einzelpreis (BigDecimal für exakte Berechnung)", example = "999.99")
        BigDecimal unitPrice,

        @Schema(description = "Gesamtpreis der Position", example = "1999.98")
        BigDecimal totalPrice
    ) {}
}
