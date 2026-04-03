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
 * REST-assured Integration Tests for CustomerResource — Solution.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CustomerResource Integration Tests")
class CustomerResourceIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/final-project/api";
    }

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

    @Test
    @Order(2)
    @DisplayName("GET /customers/1 — should return single customer")
    void shouldGetCustomerById() {
        given()
        .when()
            .get("/customers/1")
        .then()
            .statusCode(200)
            .body("name", notNullValue())
            .body("email", containsString("@"));
    }

    @Test
    @Order(3)
    @DisplayName("GET /customers/9999 — should return 404")
    void shouldReturn404WhenNotFound() {
        given()
        .when()
            .get("/customers/9999")
        .then()
            .statusCode(404)
            .body("error", containsString("not found"));
    }

    @Test
    @Order(10)
    @DisplayName("POST — create customer with JSON string")
    void shouldCreateCustomerWithJson() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Fritz Mustermann", "email": "fritz@example.com", "phone": "+43 664 9999999", "company": "TestAG"}
                """)
        .when()
            .post("/customers")
        .then()
            .statusCode(201)
            .body("name", equalTo("Fritz Mustermann"))
            .body("id", notNullValue());
    }

    @Test
    @Order(11)
    @DisplayName("POST — create customer with Java object, extract response")
    void shouldCreateAndExtractAsObject() {
        Customer input = new Customer(null, "Typed Test", "typed@example.com", "+43 660 1112233", "TypedCorp", null);

        Customer created = given()
            .contentType(ContentType.JSON)
            .body(input)
        .when()
            .post("/customers")
        .then()
            .statusCode(201)
            .extract()
            .as(Customer.class);

        assertNotNull(created.id());
        assertEquals("Typed Test", created.name());
        assertEquals("typed@example.com", created.email());
    }

    @Test
    @Order(12)
    @DisplayName("POST — empty name should return 400")
    void shouldRejectEmptyName() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "", "email": "valid@example.com"}
                """)
        .when()
            .post("/customers")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(13)
    @DisplayName("POST — invalid email should return 400")
    void shouldRejectInvalidEmail() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Bad Email", "email": "not-an-email"}
                """)
        .when()
            .post("/customers")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(14)
    @DisplayName("POST — invalid phone number should return 400")
    void shouldRejectInvalidPhone() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Bad Phone", "email": "badphone@example.com", "phone": "12345"}
                """)
        .when()
            .post("/customers")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(15)
    @DisplayName("POST — duplicate email should return 409")
    void shouldRejectDuplicateEmail() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Anna Doppelt", "email": "anna@example.com"}
                """)
        .when()
            .post("/customers")
        .then()
            .statusCode(409)
            .body("error", containsString("already exists"));
    }

    @Test
    @Order(20)
    @DisplayName("Full CRUD workflow — create, read, update, delete, verify")
    void crudWorkflow() {
        // CREATE
        Customer input = new Customer(null, "CRUD Test", "crud@example.com", "+43 664 5555555", "CrudCorp", null);
        Customer created = given()
            .contentType(ContentType.JSON)
            .body(input)
        .when()
            .post("/customers")
        .then()
            .statusCode(201)
            .extract()
            .as(Customer.class);

        Long id = created.id();

        // READ
        given()
        .when()
            .get("/customers/" + id)
        .then()
            .statusCode(200)
            .body("name", equalTo("CRUD Test"));

        // UPDATE
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "CRUD Updated", "email": "crud.updated@example.com", "phone": "+43 664 6666666", "company": "UpdatedCorp"}
                """)
        .when()
            .put("/customers/" + id)
        .then()
            .statusCode(200)
            .body("name", equalTo("CRUD Updated"));

        // DELETE
        given()
        .when()
            .delete("/customers/" + id)
        .then()
            .statusCode(204);

        // VERIFY DELETED
        given()
        .when()
            .get("/customers/" + id)
        .then()
            .statusCode(404);
    }

    @Test
    @Order(30)
    @DisplayName("Full response — validate + extract + inspect headers")
    void shouldInspectFullResponse() {
        Response response = given()
            .contentType(ContentType.JSON)
            .body("""
                {"name": "Response Test", "email": "response@example.com", "phone": "+43 664 7777777", "company": "ResponseCorp"}
                """)
        .when()
            .post("/customers")
        .then()
            .statusCode(201)
            .body("name", equalTo("Response Test"))
        .extract()
            .response();

        Customer created = response.as(Customer.class);
        assertNotNull(created.id());

        System.out.println("=== Response Headers ===");
        response.headers().asList().forEach(h ->
            System.out.println("  " + h.getName() + ": " + h.getValue()));

        System.out.println("\n=== Response Body ===");
        System.out.println(response.body().asPrettyString());
    }
}
