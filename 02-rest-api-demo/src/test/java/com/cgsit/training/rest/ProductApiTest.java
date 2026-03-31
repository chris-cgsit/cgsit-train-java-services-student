package com.cgsit.training.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * REST-assured Integration Tests for the Product API.
 *
 * <h3>What is REST-assured?</h3>
 * A Java DSL for testing REST APIs over HTTP. It sends real HTTP requests
 * to a running server and validates the responses.
 *
 * <h3>Pattern: given() / when() / then()</h3>
 * <ul>
 *   <li>{@code given()} — set up the request (headers, body, params)</li>
 *   <li>{@code when()} — execute the HTTP method (GET, POST, PUT, DELETE)</li>
 *   <li>{@code then()} — assert the response (status code, body, headers)</li>
 * </ul>
 *
 * <h3>Prerequisites</h3>
 * These tests require a running WildFly with the rest-api-demo deployed:
 * <pre>
 *   cd examples/02-rest-api-demo
 *   mvn clean package wildfly:dev
 *   # Then in another terminal:
 *   mvn test -Dtest=ProductApiTest
 * </pre>
 *
 * <p><strong>Note:</strong> These tests are skipped during normal {@code mvn package}
 * because they need a running server. Run them explicitly when the server is up.</p>
 */
@Disabled("Requires running WildFly — run manually with: mvn test -Dtest=ProductApiTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Product API — REST-assured Integration Tests")
class ProductApiTest {

    private static final String BASE_URL = "http://localhost:8080/rest-api-demo/api";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    // ================================================================
    // GET Tests
    // ================================================================

    @Test
    @Order(1)
    @DisplayName("GET /products → 200 + non-empty list")
    void getAllProducts_returnsOkWithProducts() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/products")
        .then()
            .statusCode(200)                        // HTTP 200 OK
            .contentType(ContentType.JSON)          // Response is JSON
            .body("size()", greaterThan(0))          // At least one product
            .body("[0].name", notNullValue())        // First product has a name
            .body("[0].id", greaterThan(0));          // First product has a positive ID
    }

    @Test
    @Order(2)
    @DisplayName("GET /products?category=Electronics → filtered results")
    void getAllProducts_filteredByCategory_returnsOnlyElectronics() {
        given()
            .accept(ContentType.JSON)
            .queryParam("category", "Electronics")   // Query parameter filter
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("category", everyItem(equalToIgnoringCase("Electronics")));  // All items match
    }

    @Test
    @Order(3)
    @DisplayName("GET /products/1 → 200 + product JSON")
    void getProductById_found_returnsProduct() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/products/1")                      // Path parameter
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("price", greaterThan(0f));
    }

    @Test
    @Order(4)
    @DisplayName("GET /products/999 → 404 Not Found")
    void getProductById_notFound_returns404() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/products/999")
        .then()
            .statusCode(404);                        // Not found
    }

    // ================================================================
    // POST Tests
    // ================================================================

    @Test
    @Order(5)
    @DisplayName("POST /products → 201 Created + product with ID")
    void createProduct_validData_returns201() {
        String newProduct = """
            {
                "name": "Maus",
                "price": 29.99,
                "category": "Peripherie"
            }
            """;

        given()
            .contentType(ContentType.JSON)           // Request body is JSON
            .body(newProduct)                        // JSON body
        .when()
            .post("/products")
        .then()
            .statusCode(201)                         // 201 Created
            .body("id", greaterThan(0))              // Auto-generated ID
            .body("name", equalTo("Maus"))
            .body("price", equalTo(29.99f))
            .body("category", equalTo("Peripherie"));
    }

    // ================================================================
    // PUT Tests
    // ================================================================

    @Test
    @Order(6)
    @DisplayName("PUT /products/1 → 200 Updated")
    void updateProduct_found_returns200() {
        String updatedProduct = """
            {
                "name": "Laptop Pro Max",
                "price": 1499.99,
                "category": "Electronics"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updatedProduct)
        .when()
            .put("/products/1")
        .then()
            .statusCode(200)
            .body("name", equalTo("Laptop Pro Max"))
            .body("price", equalTo(1499.99f));
    }

    @Test
    @Order(7)
    @DisplayName("PUT /products/999 → 404 Not Found")
    void updateProduct_notFound_returns404() {
        String product = """
            { "name": "Ghost", "price": 0.01, "category": "None" }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(product)
        .when()
            .put("/products/999")
        .then()
            .statusCode(404);
    }

    // ================================================================
    // DELETE Tests
    // ================================================================

    @Test
    @Order(8)
    @DisplayName("DELETE /products/1 → 204 No Content")
    void deleteProduct_found_returns204() {
        when()
            .delete("/products/1")
        .then()
            .statusCode(204);                        // 204 No Content (no body)
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /products/1 again → 404 (already deleted)")
    void deleteProduct_alreadyDeleted_returns404() {
        when()
            .delete("/products/1")
        .then()
            .statusCode(404);
    }

    @Test
    @Order(10)
    @DisplayName("GET /products/1 after delete → 404")
    void getDeletedProduct_returns404() {
        when()
            .get("/products/1")
        .then()
            .statusCode(404);
    }
}
