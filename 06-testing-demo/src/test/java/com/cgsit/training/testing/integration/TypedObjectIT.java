package com.cgsit.training.testing.integration;

import com.cgsit.training.testing.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * REST-assured — Typed Object Serialization / Deserialization.
 *
 * Instead of sending raw JSON strings and asserting on JSON paths,
 * this test sends and receives Java objects directly:
 *
 *   .body(product)         → Jackson serializes Product → JSON
 *   .extract().as(Product.class) → Jackson deserializes JSON → Product
 *
 * Advantages:
 *   - Type-safe: compiler checks field names
 *   - IDE support: autocomplete on result object
 *   - Refactor-safe: rename a field → test still compiles
 *
 * Requires jackson-databind in test classpath.
 *
 * Prerequisites: WildFly running with testing-demo deployed.
 */
@DisplayName("REST-assured — Typed Object Tests")
class TypedObjectIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/testing-demo/api";
    }

    @Test
    @DisplayName("POST — send Java object, extract response as Java object")
    void shouldCreateAndExtractAsObject() {
        // SEND: Java object as request body (Jackson serializes to JSON)
        Product input = new Product(null, "Typed Test Product", 77.50);

        // EXTRACT: response body deserialized back to Java object
        Product created = given()
            .contentType(ContentType.JSON)
            .body(input)                         // ← Java object, not JSON string
        .when()
            .post("/products")
        .then()
            .statusCode(201)
            .extract()
            .as(Product.class);                  // ← extract as typed object

        // ASSERT: on the Java object — full IDE support, type-safe
        assertNotNull(created.id(), "Server should assign an ID");
        assertEquals("Typed Test Product", created.name());
        assertEquals(77.50, created.price(), 0.001);
    }

    @Test
    @DisplayName("POST + GET — create and verify round-trip")
    void shouldRoundTripProduct() {
        Product input = new Product(null, "Round-Trip Product", 99.99);

        // CREATE
        Product created = given()
            .contentType(ContentType.JSON)
            .body(input)
        .when()
            .post("/products")
        .then()
            .statusCode(201)
            .extract()
            .as(Product.class);

        // READ back and compare
        Product fetched = given()
        .when()
            .get("/products/" + created.id())
        .then()
            .statusCode(200)
            .extract()
            .as(Product.class);

        assertEquals(created.id(), fetched.id());
        assertEquals(created.name(), fetched.name());
        assertEquals(created.price(), fetched.price(), 0.001);
    }

    @Test
    @DisplayName("GET — extract list of products as array")
    void shouldExtractProductArray() {
        Product[] products = given()
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .extract()
            .as(Product[].class);                // ← extract as typed array

        assertTrue(products.length > 0, "Should have at least one product");
        for (Product p : products) {
            assertNotNull(p.id());
            assertFalse(p.name().isBlank());
            assertTrue(p.price() > 0);
        }
    }
}
