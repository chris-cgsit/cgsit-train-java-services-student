package com.cgsit.training.datatypes.resource;

import com.cgsit.training.datatypes.model.Invoice;
import com.cgsit.training.datatypes.model.Invoice.InvoiceStatus;
import com.cgsit.training.datatypes.model.Invoice.LineItem;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Path("/invoices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Invoices", description = "Praxisbeispiel: Rechnung mit allen Datentypen")
public class InvoiceResource {

    @GET
    @Path("/sample")
    @Operation(summary = "Beispiel-Rechnung mit allen Datentypen")
    public Invoice getSample() {
        var items = List.of(
            new LineItem(1, "Laptop", 2, new BigDecimal("999.99"), new BigDecimal("1999.98")),
            new LineItem(2, "USB-C Hub", 3, new BigDecimal("45.50"), new BigDecimal("136.50")),
            new LineItem(3, "Monitor 27\"", 1, new BigDecimal("349.00"), new BigDecimal("349.00"))
        );

        BigDecimal net = new BigDecimal("2485.48");
        BigDecimal taxRate = new BigDecimal("20.0");
        BigDecimal gross = net.multiply(BigDecimal.ONE.add(taxRate.divide(new BigDecimal("100"))));

        return new Invoice(
            1001,
            9_000_000_001L,
            "Max Mustermann",
            "max@example.com",
            2485.48,                            // double — Rundungsfehler möglich!
            net,                                // BigDecimal — exakt
            taxRate,
            gross,
            false,
            LocalDate.of(2026, 3, 31),
            LocalDate.of(2026, 4, 30),
            LocalTime.of(14, 30, 0),
            LocalDateTime.now(),
            InvoiceStatus.OPEN,
            items,
            Map.of("bestellnummer", "B-2026-0042", "abteilung", "IT")
        );
    }

    @POST
    @Operation(summary = "Rechnung empfangen und zurückgeben (Round-Trip Test)")
    public Invoice roundTrip(Invoice invoice) {
        return invoice;
    }

    @GET
    @Path("/precision-test")
    @Operation(summary = "double vs BigDecimal — Rundungsfehler demonstrieren",
               description = "Zeigt warum man für Geldbeträge BigDecimal statt double verwenden muss")
    public Map<String, Object> precisionTest() {
        // double: 0.1 + 0.2 = 0.30000000000000004 (!)
        double doubleResult = 0.1 + 0.2;

        // BigDecimal: 0.1 + 0.2 = 0.3 (exakt)
        BigDecimal bdResult = new BigDecimal("0.1").add(new BigDecimal("0.2"));

        // 19.99 * 100 Stück
        double doubleTotalWrong = 19.99 * 100;
        BigDecimal bdTotalCorrect = new BigDecimal("19.99").multiply(new BigDecimal("100"));

        return Map.of(
            "test_1_addition", Map.of(
                "calculation", "0.1 + 0.2",
                "double_result", doubleResult,
                "bigdecimal_result", bdResult,
                "double_correct", doubleResult == 0.3,
                "bigdecimal_correct", bdResult.compareTo(new BigDecimal("0.3")) == 0
            ),
            "test_2_multiplication", Map.of(
                "calculation", "19.99 × 100",
                "double_result", doubleTotalWrong,
                "bigdecimal_result", bdTotalCorrect,
                "expected", 1999.00
            ),
            "recommendation", "Für Geldbeträge IMMER BigDecimal verwenden!"
        );
    }
}
