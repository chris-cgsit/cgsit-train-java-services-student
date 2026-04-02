package com.cgsit.training.testing.unit;

import com.cgsit.training.testing.model.Product;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Validation")
class ProductValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("gueltiges Produkt hat keine Violations")
    void validProductHasNoViolations() {
        Product product = new Product(1L, "Laptop", 999.99);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("leerer Name erzeugt Violation")
    void emptyNameCausesViolation() {
        Product product = new Product(1L, "", 999.99);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("negativer Preis erzeugt Violation")
    void negativePriceCausesViolation() {
        Product product = new Product(1L, "Laptop", -5.0);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }

    @Test
    @DisplayName("zu kurzer Name erzeugt Violation")
    void tooShortNameCausesViolation() {
        Product product = new Product(1L, "X", 10.0);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("mehrere ungueltige Felder erzeugen mehrere Violations")
    void multipleInvalidFieldsCauseMultipleViolations() {
        Product product = new Product(1L, "", -1.0);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertTrue(violations.size() >= 2,
                "Erwartet mindestens 2 Violations, aber nur " + violations.size() + " gefunden");
    }
}
