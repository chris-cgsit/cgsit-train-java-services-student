package com.cgsit.training.bookapi.integration;

import com.cgsit.training.bookapi.model.Book;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * REST-assured Integration Tests for BookResource — Solution.
 *
 * Prerequisites:
 *   1. WildFly running with ex05-testing deployed
 *   2. mvn clean package wildfly:dev
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BookResource Integration Tests")
class BookResourceIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/ex05-testing/api";
    }

    @Test
    @Order(1)
    @DisplayName("GET /books — should return list of books")
    void shouldListBooks() {
        given()
        .when()
            .get("/books")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0));
    }

    @Test
    @Order(2)
    @DisplayName("GET /books/1 — should return single book")
    void shouldGetBookById() {
        given()
        .when()
            .get("/books/1")
        .then()
            .statusCode(200)
            .body("title", notNullValue())
            .body("price", greaterThan(0f));
    }

    @Test
    @Order(3)
    @DisplayName("GET /books/9999 — should return 404")
    void shouldReturn404WhenNotFound() {
        given()
        .when()
            .get("/books/9999")
        .then()
            .statusCode(404);
    }

    @Test
    @Order(10)
    @DisplayName("POST /books — should create book with JSON string")
    void shouldCreateBookWithJson() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "Test Book", "author": "Test Author",
                 "isbn": "978-0000000001", "price": 29.99}
                """)
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .body("title", equalTo("Test Book"))
            .body("author", equalTo("Test Author"))
            .body("price", equalTo(29.99f));
    }

    @Test
    @Order(11)
    @DisplayName("POST /books — should create book with Java object and extract response")
    void shouldCreateAndExtractAsObject() {
        // Send Java object
        Book input = new Book(0, "Typed Book", "Typed Author", "978-0000000002", 39.99);

        // Extract response as typed object
        Book created = given()
            .contentType(ContentType.JSON)
            .body(input)
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .as(Book.class);

        // Assert on the Java object
        assertTrue(created.id() > 0, "Server should assign an ID");
        assertEquals("Typed Book", created.title());
        assertEquals("Typed Author", created.author());
        assertEquals(39.99, created.price(), 0.001);
    }

    @Test
    @Order(12)
    @DisplayName("POST /books — empty title should return 400")
    void shouldRejectEmptyTitle() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "", "author": "Author", "isbn": "978-0000000003", "price": 10.00}
                """)
        .when()
            .post("/books")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(20)
    @DisplayName("Full CRUD — create, read, update, delete")
    void shouldPerformCrudWorkflow() {
        // CREATE
        int id = given()
            .contentType(ContentType.JSON)
            .body("""
                {"title": "CRUD Book", "author": "CRUD Author",
                 "isbn": "978-0000000004", "price": 19.99}
                """)
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract().path("id");

        // READ
        given()
        .when()
            .get("/books/" + id)
        .then()
            .statusCode(200)
            .body("title", equalTo("CRUD Book"));

        // DELETE
        given()
        .when()
            .delete("/books/" + id)
        .then()
            .statusCode(204);

        // VERIFY DELETED
        given()
        .when()
            .get("/books/" + id)
        .then()
            .statusCode(404);
    }
}
