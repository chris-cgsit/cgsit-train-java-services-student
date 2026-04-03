package com.cgsit.training.customer.integration;

import com.cgsit.training.customer.model.Customer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * REST-assured Integration Tests for CustomerResource.
 *
 * Prerequisites:
 *   1. WildFly running with final-project deployed
 *   2. mvn clean package wildfly:dev
 *   3. ServiceExceptionMapper muss implementiert sein (sonst kommen 500 statt 404/409)
 *
 * TODO: Implementiere die markierten Tests.
 *
 * Hinweise:
 *   - given().body(customer) sendet ein Java-Objekt als JSON
 *   - .extract().as(Customer.class) deserialisiert die Response
 *   - .extract().response() gibt die volle Response (Headers + Body)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CustomerResource Integration Tests")
class CustomerResourceIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/final-project/api";
    }

    // ========== Beispiel (fertig) ==========

    @Test
    @Order(1)
    @DisplayName("GET /customers — should return list")
    void shouldListCustomers() {
        given()
        .when()
            .get("/customers")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0))
            .body("[0].name", notNullValue());
    }

    // ========== TODO: Implementiere die folgenden Tests ==========

    @Test
    @Order(2)
    @DisplayName("GET /customers/1 — should return single customer")
    void shouldGetCustomerById() {
        // TODO: GET /customers/1
        //   → statusCode 200
        //   → body "name" is notNullValue()
        //   → body "email" containsString("@")
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(3)
    @DisplayName("GET /customers/9999 — should return 404")
    void shouldReturn404WhenNotFound() {
        // TODO: GET /customers/9999
        //   → statusCode 404
        //   → body "error" containsString("not found")
        //
        // Hinweis: Funktioniert erst wenn ServiceExceptionMapper implementiert ist!
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(10)
    @DisplayName("POST — create customer with JSON string")
    void shouldCreateCustomerWithJson() {
        // TODO: POST /customers mit JSON body:
        //   {"name": "Fritz Mustermann", "email": "fritz@example.com", "company": "TestAG"}
        //   → statusCode 201
        //   → body "name" equalTo "Fritz Mustermann"
        //   → body "id" notNullValue()
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(11)
    @DisplayName("POST — create customer with Java object, extract response")
    void shouldCreateAndExtractAsObject() {
        // TODO:
        //   1. Erstelle: new Customer(null, "Typed Test", "typed@example.com", "TypedCorp", null)
        //   2. Sende mit .body(customer)
        //   3. Extrahiere mit .extract().as(Customer.class)
        //   4. Prüfe: id ist nicht null, name ist "Typed Test", email ist "typed@example.com"
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(12)
    @DisplayName("POST — empty name should return 400")
    void shouldRejectEmptyName() {
        // TODO: POST mit leerem Name → statusCode 400
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(13)
    @DisplayName("POST — invalid email should return 400")
    void shouldRejectInvalidEmail() {
        // TODO: POST mit email "not-an-email" → statusCode 400
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(14)
    @DisplayName("POST — invalid phone number should return 400")
    void shouldRejectInvalidPhone() {
        // TODO: POST mit phone "12345" (falsches Format)
        //   → statusCode 400
        //
        // Hinweis: Funktioniert erst wenn @Pattern auf phone im Customer-Record gesetzt ist!
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(15)
    @DisplayName("POST — duplicate email should return 409")
    void shouldRejectDuplicateEmail() {
        // TODO: POST mit email "anna@example.com" (existiert bereits!)
        //   → statusCode 409
        //   → body "error" containsString("already exists")
        //
        // Hinweis: Funktioniert erst wenn ServiceExceptionMapper implementiert ist!
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(20)
    @DisplayName("Full CRUD workflow — create, read, update, delete, verify")
    void crudWorkflow() {
        // TODO: Implementiere den kompletten CRUD Workflow:
        //
        // 1. POST: Customer erstellen, Response extrahieren als Customer-Objekt
        //    → statusCode 201
        //
        // 2. GET: Customer mit der id abrufen
        //    → statusCode 200, name prüfen
        //
        // 3. PUT: Customer aktualisieren (neuen Name setzen)
        //    → statusCode 200, neuen Name prüfen
        //
        // 4. DELETE: Customer löschen
        //    → statusCode 204
        //
        // 5. GET: Customer nochmal abrufen
        //    → statusCode 404
        //
        // Tipp: .extract().as(Customer.class) für das Objekt,
        //       customer.id() für die ID im nächsten Request
        fail("TODO: Implementiere diesen Test");
    }

    @Test
    @Order(30)
    @DisplayName("Full response — validate + extract + inspect headers")
    void shouldInspectFullResponse() {
        // TODO:
        //   1. POST einen neuen Customer
        //   2. Validiere mit .then(): statusCode 201, body "name" prüfen
        //   3. .extract().response() für die volle Response
        //   4. Response.as(Customer.class) für das Objekt
        //   5. System.out.println: alle Headers ausgeben
        //   6. System.out.println: Response Body als Pretty String
        //
        // Tipp: response.headers().asList().forEach(h -> ...)
        fail("TODO: Implementiere diesen Test");
    }
}
