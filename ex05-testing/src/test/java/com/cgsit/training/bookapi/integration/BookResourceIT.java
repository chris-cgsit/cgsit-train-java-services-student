package com.cgsit.training.bookapi.integration;

import com.cgsit.training.bookapi.model.Book;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * REST-assured Integration Tests for BookResource.
 *
 * Prerequisites:
 *   1. WildFly running with ex05-testing deployed
 *   2. mvn clean package wildfly:dev
 *
 * Aufgabe: Implementiere die markierten TODO-Tests.
 * Nutze .body(book) zum Senden und .extract().as(Book.class) zum Empfangen.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BookResource Integration Tests")
class BookResourceIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/ex05-testing/api";
    }

    // ========== Beispiel (fertig implementiert) ==========

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

    // ========== TODO: Implementiere die folgenden Tests ==========

    @Test
    @Order(2)
    @DisplayName("GET /books/1 — should return single book")
    void shouldGetBookById() {
        // TODO: GET /books/1 → statusCode 200, body "title" is notNullValue()
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(3)
    @DisplayName("GET /books/9999 — should return 404")
    void shouldReturn404WhenNotFound() {
        // TODO: GET /books/9999 → statusCode 404
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(10)
    @DisplayName("POST /books — should create book with JSON string")
    void shouldCreateBookWithJson() {
        // TODO: POST mit JSON string body:
        //   {"title": "Test Book", "author": "Test Author", "isbn": "978-0000000001", "price": 29.99}
        // → statusCode 201, body "title" equalTo "Test Book"
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(11)
    @DisplayName("POST /books — should create book with Java object and extract response")
    void shouldCreateAndExtractAsObject() {
        // TODO:
        // 1. Erstelle ein Book-Objekt: new Book(0, "Typed Book", "Author", "978-0000000002", 39.99)
        // 2. Sende es mit .body(book) als Request
        // 3. Extrahiere die Response mit .extract().as(Book.class)
        // 4. Prüfe: id ist nicht 0, title ist "Typed Book", price ist 39.99
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(12)
    @DisplayName("POST /books — empty title should return 400")
    void shouldRejectEmptyTitle() {
        // TODO: POST mit leerem Titel → statusCode 400
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(20)
    @DisplayName("Full CRUD — create, read, update, delete")
    void shouldPerformCrudWorkflow() {
        // TODO:
        // 1. POST: Buch erstellen, id extrahieren mit .extract().path("id")
        // 2. GET: Buch mit der id abrufen, title prüfen
        // 3. DELETE: Buch löschen → statusCode 204
        // 4. GET: Buch nochmal abrufen → statusCode 404
        fail("TODO: Implementiere diesen Test");
    }
}
