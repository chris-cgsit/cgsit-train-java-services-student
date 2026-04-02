package com.cgsit.training.testing.integration;

import com.cgsit.training.testing.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * REST-assured — Full Response extraction.
 *
 * .extract().response() is the most flexible approach:
 *   1. Validate with Hamcrest matchers (JSON path assertions)
 *   2. Extract typed Java object with .as(Class)
 *   3. Access headers, status code, cookies
 *
 * All in one test — no information lost.
 *
 * Prerequisites: WildFly running with testing-demo deployed.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("REST-assured — Full Response Extraction")
class FullResponseIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/testing-demo/api";
    }

    @Test
    @Order(1)
    @DisplayName("POST — validate JSON + extract object + inspect headers")
    void shouldValidateAndExtractAndInspectHeaders() {
        Product input = new Product(null, "Full Response Test", 77.50);

        // Step 1: Send request, validate with Hamcrest, extract full response
        Response response = given()
            .contentType(ContentType.JSON)
            .body(input)
        .when()
            .post("/products")
        .then()
            // Hamcrest validation on JSON (still works)
            .statusCode(201)
            .body("name", equalTo("Full Response Test"))
            .body("price", greaterThan(0f))
            .body("id", notNullValue())
        .extract()
            .response();       // ← full response, nothing lost

        // Step 2: Extract as typed Java object
        Product created = response.as(Product.class);
        assertNotNull(created.id(), "Server should assign an ID");
        assertEquals("Full Response Test", created.name());
        assertEquals(77.50, created.price(), 0.001);

        // Step 3: Inspect status code
        assertEquals(201, response.statusCode());

        // Step 4: Inspect individual headers
        String contentType = response.header("Content-Type");
        String location = response.header("Location");
        System.out.println("=== Response Details ===");
        System.out.println("Status:       " + response.statusCode());
        System.out.println("Content-Type: " + contentType);
        System.out.println("Location:     " + location);

        // Step 5: Print ALL response headers
        System.out.println();
        System.out.println("=== ALL Response Headers ===");
        response.headers().asList().forEach(header ->
            System.out.println("  " + header.getName() + ": " + header.getValue())
        );

        // Step 6: Raw response body as string
        System.out.println();
        System.out.println("=== Raw Body ===");
        System.out.println(response.body().asPrettyString());
    }

    @Test
    @Order(2)
    @DisplayName("GET list — validate + extract array + print headers")
    void shouldExtractListAndHeaders() {
        Response response = given()
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].name", notNullValue())
        .extract()
            .response();

        // Extract as typed array
        Product[] products = response.as(Product[].class);
        assertTrue(products.length > 0);

        System.out.println("=== Product List ===");
        System.out.println("Count: " + products.length);
        for (Product p : products) {
            System.out.println("  [" + p.id() + "] " + p.name() + " — " + p.price());
        }

        // Print headers
        System.out.println();
        System.out.println("=== Response Headers ===");
        response.headers().asList().forEach(header ->
            System.out.println("  " + header.getName() + ": " + header.getValue())
        );
    }

    @Test
    @Order(3)
    @DisplayName("GET single — extract individual fields via path()")
    void shouldExtractIndividualFields() {
        Response response = given()
        .when()
            .get("/products/1")
        .then()
            .statusCode(200)
        .extract()
            .response();

        // Extract individual fields (no deserialization needed)
        Long id = response.path("id");
        String name = response.path("name");
        Float price = response.path("price");

        System.out.println("=== Individual Fields ===");
        System.out.println("  id:    " + id);
        System.out.println("  name:  " + name);
        System.out.println("  price: " + price);

        assertNotNull(id);
        assertNotNull(name);
        assertTrue(price > 0);
    }

    @Test
    @Order(4)
    @DisplayName("Error response — inspect 404 details")
    void shouldInspect404Response() {
        Response response = given()
        .when()
            .get("/products/9999")
        .then()
            .statusCode(404)
        .extract()
            .response();

        System.out.println("=== 404 Error Response ===");
        System.out.println("Status:  " + response.statusCode());
        System.out.println("Body:    " + response.body().asPrettyString());
        System.out.println();
        System.out.println("=== Headers ===");
        response.headers().asList().forEach(header ->
            System.out.println("  " + header.getName() + ": " + header.getValue())
        );

        // Response time
        System.out.println();
        System.out.println("Response time: " + response.time() + " ms");
    }
}
