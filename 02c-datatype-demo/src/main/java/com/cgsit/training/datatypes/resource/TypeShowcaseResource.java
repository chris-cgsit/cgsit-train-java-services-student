package com.cgsit.training.datatypes.resource;

import com.cgsit.training.datatypes.model.TypeShowcase;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Type Showcase", description = "Alle Java ↔ JSON Typmappings auf einen Blick")
public class TypeShowcaseResource {

    @GET
    @Operation(summary = "Alle Datentypen als JSON zurückgeben",
               description = "Zeigt wie jeder Java-Typ in JSON serialisiert wird")
    public TypeShowcase getAll() {
        return new TypeShowcase(
            // Primitive
            (byte) 127,
            (short) 32000,
            42,
            9_000_000_001L,
            3.14f,
            3.141592653589793,
            true,
            'A',

            // Exakte Zahlen
            new BigDecimal("19999.99"),
            new BigInteger("123456789012345678901234567890"),

            // Text
            "Hallo Welt",
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),

            // Datum & Zeit
            LocalDate.of(2026, 3, 31),
            LocalTime.of(14, 30, 0),
            LocalDateTime.of(2026, 3, 31, 14, 30, 0),
            OffsetDateTime.of(2026, 3, 31, 14, 30, 0, 0, ZoneOffset.ofHours(2)),
            Instant.parse("2026-03-31T12:30:00Z"),
            Duration.ofHours(1).plusMinutes(30),

            // Collections
            List.of("Wien", "Graz", "Linz"),
            List.of(1, 2, 3, 5, 8, 13),
            Map.of("sprache", "Deutsch", "land", "Österreich"),
            Map.of("name", "Max", "alter", 30, "aktiv", true),

            // Nullable
            null
        );
    }

    @POST
    @Operation(summary = "JSON empfangen und zurückgeben (Round-Trip Test)",
               description = "Sendet ein TypeShowcase JSON und gibt es unverändert zurück — prüft Deserialisierung")
    public TypeShowcase roundTrip(TypeShowcase input) {
        return input;
    }
}
