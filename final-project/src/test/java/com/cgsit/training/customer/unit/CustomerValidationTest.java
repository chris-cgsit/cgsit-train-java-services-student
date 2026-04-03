package com.cgsit.training.customer.unit;

import com.cgsit.training.customer.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: Unit Tests fuer Customer Bean Validation + Jackson Serialization.
 *
 * Diese Tests laufen OHNE Server — nur Hibernate Validator + Jackson.
 *
 * Aufgabe: Implementiere die markierten Tests.
 *
 * Hinweise:
 *   - validator.validate(customer) gibt ein Set<ConstraintViolation> zurueck
 *   - Leeres Set = keine Fehler = valider Customer
 *   - mapper.writeValueAsString(customer) serialisiert zu JSON
 *   - mapper.readValue(json, Customer.class) deserialisiert
 */
@DisplayName("Customer Validation & Serialization Tests")
class CustomerValidationTest {

    static Validator validator;
    static ObjectMapper mapper;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new com.fasterxml.jackson.module.paramnames.ParameterNamesModule());
    }

    static Customer validCustomer() {
        return new Customer(1L, "Anna Meier", "anna@example.com",
                "+43 664 1234567", "CGS IT Solutions", LocalDateTime.now());
    }

    // ========== VALIDATION (Beispiel fertig) ==========

    @Test
    @DisplayName("Valid customer — no violations")
    void shouldPassValidation() {
        Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer());
        assertTrue(violations.isEmpty(), "Expected no violations but got: " + violations);
    }

    // ========== TODO: Validation Tests ==========

    @Test
    @DisplayName("Empty name — @NotBlank violation")
    void shouldRejectEmptyName() {
        // TODO: Erstelle Customer mit leerem Name
        //   → validator.validate() muss Violations enthalten
        //   → pruefe dass "name" Feld betroffen ist
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @DisplayName("Invalid email — @Email violation")
    void shouldRejectInvalidEmail() {
        // TODO: Erstelle Customer mit email "not-an-email"
        //   → validator.validate() muss Violations fuer "email" enthalten
        fail("TODO: Implementiere diesen Test");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "0664-1234567", "+43", "abc"})
    @DisplayName("Invalid phone formats — @Pattern violation")
    void shouldRejectInvalidPhone(String phone) {
        // TODO: Erstelle Customer mit dem uebergebenen phone Wert
        //   → validator.validate() muss Violations fuer "phone" enthalten
        //
        // Hinweis: Funktioniert erst wenn @Pattern auf phone im Model gesetzt ist!
        fail("TODO: Implementiere diesen Test");
    }

    @ParameterizedTest
    @ValueSource(strings = {"+43 664 1234567", "+49 170 9876543", "+1 555 1234567"})
    @DisplayName("Valid phone formats — no violation")
    void shouldAcceptValidPhone(String phone) {
        // TODO: Erstelle Customer mit dem uebergebenen phone Wert
        //   → validator.validate() darf KEINE Violation fuer "phone" haben
        fail("TODO: Implementiere diesen Test");
    }

    // ========== TODO: Serialization Tests ==========

    @Test
    @DisplayName("Serialize customer to JSON")
    void shouldSerializeToJson() throws JsonProcessingException {
        // TODO: mapper.writeValueAsString(validCustomer())
        //   → JSON muss "name", "Anna Meier", "phone" enthalten
        //   → Pruefe mit assertTrue(json.contains("..."))
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @DisplayName("Null fields are excluded (@JsonInclude NON_NULL)")
    void shouldExcludeNullFields() throws JsonProcessingException {
        // TODO: Erstelle Customer mit company=null und createdAt=null
        //   → mapper.writeValueAsString()
        //   → JSON darf NICHT "company" oder "createdAt" enthalten
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @DisplayName("Deserialize JSON to customer")
    void shouldDeserializeFromJson() throws JsonProcessingException {
        // TODO: JSON String mit name, email, phone, company
        //   → mapper.readValue(json, Customer.class)
        //   → Pruefe alle Felder mit assertEquals
        fail("TODO: Implementiere diesen Test");
    }
}
