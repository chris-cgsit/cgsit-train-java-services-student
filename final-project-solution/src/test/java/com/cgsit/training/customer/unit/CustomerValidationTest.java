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
 * Unit Tests for Customer Bean Validation + Jackson Serialization.
 *
 * Tests run WITHOUT a server — only Hibernate Validator + Jackson.
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

    // ========== VALIDATION ==========

    @Nested
    @DisplayName("Bean Validation")
    class ValidationTests {

        @Test
        @DisplayName("Valid customer — no violations")
        void shouldPassValidation() {
            Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer());
            assertTrue(violations.isEmpty(), "Expected no violations but got: " + violations);
        }

        @Test
        @DisplayName("Empty name — @NotBlank violation")
        void shouldRejectEmptyName() {
            Customer c = new Customer(null, "", "a@b.com", "+43 664 1234567", null, null);
            var violations = validator.validate(c);
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        }

        @Test
        @DisplayName("Name too short — @Size violation")
        void shouldRejectShortName() {
            Customer c = new Customer(null, "A", "a@b.com", "+43 664 1234567", null, null);
            var violations = validator.validate(c);
            assertFalse(violations.isEmpty());
        }

        @Test
        @DisplayName("Invalid email — @Email violation")
        void shouldRejectInvalidEmail() {
            Customer c = new Customer(null, "Anna", "not-an-email", "+43 664 1234567", null, null);
            var violations = validator.validate(c);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "0664-1234567", "+43", "abc", "+43664 1234567"})
        @DisplayName("Invalid phone formats — @Pattern violation")
        void shouldRejectInvalidPhone(String phone) {
            Customer c = new Customer(null, "Anna", "a@b.com", phone, null, null);
            var violations = validator.validate(c);
            assertFalse(violations.isEmpty(),
                    "Expected violation for phone: " + phone);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"+43 664 1234567", "+49 170 9876543", "+1 555 1234567"})
        @DisplayName("Valid phone formats — no violation")
        void shouldAcceptValidPhone(String phone) {
            Customer c = new Customer(null, "Anna", "a@b.com", phone, null, null);
            var violations = validator.validate(c);
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("phone")),
                    "Expected no phone violation for: " + phone);
        }

        @Test
        @DisplayName("Multiple violations at once")
        void shouldReportMultipleViolations() {
            Customer c = new Customer(null, "", "bad-email", "12345", null, null);
            var violations = validator.validate(c);
            assertTrue(violations.size() >= 3,
                    "Expected at least 3 violations but got: " + violations.size());
        }
    }

    // ========== JACKSON SERIALIZATION ==========

    @Nested
    @DisplayName("Jackson Serialization")
    class SerializationTests {

        @Test
        @DisplayName("Serialize customer to JSON")
        void shouldSerializeToJson() throws JsonProcessingException {
            String json = mapper.writeValueAsString(validCustomer());

            assertTrue(json.contains("\"name\""));
            assertTrue(json.contains("Anna Meier"));
            assertTrue(json.contains("\"email\""));
            assertTrue(json.contains("\"phone\""));
            assertTrue(json.contains("+43 664 1234567"));
        }

        @Test
        @DisplayName("Null fields are excluded (@JsonInclude NON_NULL)")
        void shouldExcludeNullFields() throws JsonProcessingException {
            Customer c = new Customer(1L, "Anna", "a@b.com", "+43 664 1234567", null, null);
            String json = mapper.writeValueAsString(c);

            assertFalse(json.contains("\"company\""), "Null company should be excluded");
            assertFalse(json.contains("\"createdAt\""), "Null createdAt should be excluded");
        }

        @Test
        @DisplayName("Deserialize JSON to customer")
        void shouldDeserializeFromJson() throws JsonProcessingException {
            String json = """
                    {"name": "Bob", "email": "bob@test.com", "phone": "+49 170 9876543", "company": "TestAG"}
                    """;

            Customer c = mapper.readValue(json, Customer.class);

            assertEquals("Bob", c.name());
            assertEquals("bob@test.com", c.email());
            assertEquals("+49 170 9876543", c.phone());
            assertEquals("TestAG", c.company());
        }

        @Test
        @DisplayName("Field order matches @JsonPropertyOrder")
        void shouldRespectFieldOrder() throws JsonProcessingException {
            String json = mapper.writeValueAsString(validCustomer());

            int idPos = json.indexOf("\"id\"");
            int namePos = json.indexOf("\"name\"");
            int emailPos = json.indexOf("\"email\"");
            int phonePos = json.indexOf("\"phone\"");

            assertTrue(idPos < namePos, "id should come before name");
            assertTrue(namePos < emailPos, "name should come before email");
            assertTrue(emailPos < phonePos, "email should come before phone");
        }

        @Test
        @DisplayName("Round-trip: serialize → deserialize")
        void shouldRoundTrip() throws JsonProcessingException {
            Customer original = validCustomer();
            String json = mapper.writeValueAsString(original);
            Customer restored = mapper.readValue(json, Customer.class);

            assertEquals(original.name(), restored.name());
            assertEquals(original.email(), restored.email());
            assertEquals(original.phone(), restored.phone());
            assertEquals(original.company(), restored.company());
        }
    }
}
