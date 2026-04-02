package com.cgsit.training.testing.integration;

import com.cgsit.training.testing.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * REST-assured Integration Tests for ProductResource.
 *
 * Prerequisites:
 *   1. WildFly running with testing-demo deployed
 *   2. mvn clean package wildfly:dev
 *
 * These tests run AGAINST a live server — they are not unit tests.
 * Convention: *IT.java → executed by Maven Failsafe (mvn verify),
 * but can also run from IntelliJ directly.
 *
 * Pattern: Given-When-Then (Arrange-Act-Assert)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ProductResource Integration Tests")
class ProductResourceIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/testing-demo/api";
    }

    // ========== GET ==========

    @Test
    @Order(1)
    @DisplayName("GET /products — should return list of products")
    void shouldListProducts() {
        given()
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0))
            .body("[0].name", notNullValue())
            .body("[0].price", greaterThan(0f));
    }

    @Test
    @Order(2)
    @DisplayName("GET /products/1 — should return single product")
    void shouldGetProductById() {
        given()
        .when()
            .get("/products/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("price", greaterThan(0f));
    }

    @Test
    @Order(3)
    @DisplayName("GET /products/9999 — should return 404")
    void shouldReturn404WhenNotFound() {
        given()
        .when()
            .get("/products/9999")
        .then()
            .statusCode(404);
    }

    // ========== POST ==========

    @Test
    @Order(10)
    @DisplayName("POST /products — should create product and return 201")
    void shouldCreateProduct() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "REST-assured Test Product", "price": 42.00}
                """)
        .when()
            .post("/products")
        .then()
            .statusCode(201)
            .header("Location", containsString("/products/"))
            .body("name", equalTo("REST-assured Test Product"))
            .body("price", equalTo(42.0f))
            .body("id", notNullValue());
    }

    @Test
    @Order(10)
    @DisplayName("POST — send Java object, extract response as Java object")
    void shouldCreateAndExtractAsObject() {
        // SEND: Java object directly as request body (Jackson serializes it)
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

        // VERIFY: read back from server and compare
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
    @Order(11)
    @DisplayName("POST /products — empty name should return 400")
    void shouldRejectEmptyName() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "", "price": 10.00}
                """)
        .when()
            .post("/products")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(12)
    @DisplayName("POST /products — negative price should return 400")
    void shouldRejectNegativePrice() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Bad Product", "price": -5.00}
                """)
        .when()
            .post("/products")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(13)
    @DisplayName("POST /products — multiple violations should all be reported")
    void shouldReportAllViolations() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "", "price": -1.00}
                """)
        .when()
            .post("/products")
        .then()
            .statusCode(400);
    }

    // ========== PUT ==========

    @Test
    @Order(20)
    @DisplayName("PUT /products/1 — should update product")
    void shouldUpdateProduct() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Updated Product", "price": 99.99}
                """)
        .when()
            .put("/products/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", equalTo("Updated Product"))
            .body("price", equalTo(99.99f));
    }

    @Test
    @Order(21)
    @DisplayName("PUT /products/9999 — should return 404")
    void shouldReturn404OnUpdateNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Ghost", "price": 10.00}
                """)
        .when()
            .put("/products/9999")
        .then()
            .statusCode(404);
    }

    // ========== DELETE ==========

    @Test
    @Order(30)
    @DisplayName("DELETE /products/1 — should return 204")
    void shouldDeleteProduct() {
        given()
        .when()
            .delete("/products/1")
        .then()
            .statusCode(204);
    }

    @Test
    @Order(31)
    @DisplayName("DELETE /products/9999 — should return 404")
    void shouldReturn404OnDeleteNotFound() {
        given()
        .when()
            .delete("/products/9999")
        .then()
            .statusCode(404);
    }

    // ========== CRUD Workflow ==========

    @Test
    @Order(40)
    @DisplayName("Full CRUD workflow — create, read, update, delete")
    void crudWorkflow() {
        // CREATE
        int id = given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Workflow Test", "price": 55.00}
                """)
        .when()
            .post("/products")
        .then()
            .statusCode(201)
            .extract().path("id");

        // READ
        given()
        .when()
            .get("/products/" + id)
        .then()
            .statusCode(200)
            .body("name", equalTo("Workflow Test"));

        // UPDATE
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Workflow Updated", "price": 66.00}
                """)
        .when()
            .put("/products/" + id)
        .then()
            .statusCode(200)
            .body("name", equalTo("Workflow Updated"));

        // DELETE
        given()
        .when()
            .delete("/products/" + id)
        .then()
            .statusCode(204);

        // VERIFY DELETED
        given()
        .when()
            .get("/products/" + id)
        .then()
            .statusCode(404);
    }

    // ========================================================================
    // AUTH EXAMPLES (commented out)
    //
    // So würden die Tests aussehen, wenn die API abgesichert wäre
    // (z.B. mit 07-security-demo Konfiguration: Basic Auth oder JWT).
    // ========================================================================

    /*
    // --- HTTP Basic Auth ---

    @Test
    @DisplayName("Basic Auth — authenticated user can list products")
    void shouldListProductsWithBasicAuth() {
        given()
            .auth().basic("alice", "alice123")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    @DisplayName("Basic Auth — no credentials returns 401")
    void shouldReturn401WithoutAuth() {
        given()
        .when()
            .get("/products")
        .then()
            .statusCode(401);
    }

    @Test
    @DisplayName("Basic Auth — wrong password returns 401")
    void shouldReturn401WithWrongPassword() {
        given()
            .auth().basic("alice", "wrong-password")
        .when()
            .get("/products")
        .then()
            .statusCode(401);
    }

    @Test
    @DisplayName("Basic Auth — user without admin role gets 403")
    void shouldReturn403ForNonAdmin() {
        given()
            .auth().basic("alice", "alice123")  // role: user (not admin)
        .when()
            .get("/admin/settings")
        .then()
            .statusCode(403);
    }

    // --- Bearer Token (JWT) ---

    @Test
    @DisplayName("Bearer Token — authenticated with JWT")
    void shouldListProductsWithBearerToken() {
        String token = "eyJhbGciOiJSUzI1NiJ9...";  // from Keycloak or test token

        given()
            .auth().oauth2(token)
        .when()
            .get("/products")
        .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Bearer Token — expired token returns 401")
    void shouldReturn401WithExpiredToken() {
        String expiredToken = "eyJhbGciOiJSUzI1NiJ9.expired...";

        given()
            .auth().oauth2(expiredToken)
        .when()
            .get("/products")
        .then()
            .statusCode(401);
    }

    // --- Custom API Key ---

    @Test
    @DisplayName("API Key — valid key in custom header")
    void shouldAcceptApiKey() {
        given()
            .header("X-API-Key", "training-key-2025")
        .when()
            .get("/products")
        .then()
            .statusCode(200);
    }
    */
}
