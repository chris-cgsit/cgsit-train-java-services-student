# Abschlussprojekt — Customer API

## Szenario

REST API fuer Kunden-Verwaltung mit Bean Validation, Jackson, OpenAPI, Error Handling und Testing.

## Was ist FERTIG

- `Customer` Record mit Bean Validation + Jackson Annotations
- `CustomerStore` In-Memory Repository (5 Beispielkunden)
- `CustomerService` Business-Logik (Duplicate-Check, Not-Found)
- `CustomerResource` REST Endpoints (GET, POST, PUT, DELETE)
- `ServiceException` / `CustomerNotFoundException` / `DuplicateCustomerException`
- Swagger UI (`swagger-ui.html`)
- IntelliJ HTTP Tests (`api-tests.http`)
- Mockito Unit Tests (`CustomerServiceTest` — alle Tests gruen)

## TODO (Aufgaben)

### 1. ServiceExceptionMapper implementieren

Datei: `mapper/ServiceExceptionMapper.java`

- `@Provider` Annotation hinzufuegen
- `toResponse()` implementieren: Status + JSON Error Body
- Ohne Mapper: 500 statt 404/409

### 2. REST-assured Integration Tests implementieren

Datei: `integration/CustomerResourceIT.java`

- 10 Tests mit TODO-Markierung
- Verschiedene Szenarien: GET, POST, Validation, Duplicate, CRUD Workflow
- `.body(customer)` und `.extract().as(Customer.class)` verwenden

## Bewertungskriterien

1. ServiceExceptionMapper liefert 404/409 mit JSON Body (nicht 500)
2. Mindestens 5 REST-assured Tests gruen
3. CRUD Workflow Test funktioniert
4. Typed Object Test mit `.extract().as(Customer.class)`
5. Swagger UI zeigt die API korrekt an

## Starten

```bash
cd final-project/starter
mvn clean package wildfly:dev
```

Swagger UI: http://localhost:8080/final-project/swagger-ui.html

## Zeit

90 Minuten
