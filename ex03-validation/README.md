# Übung 3 — Bean Validation

## Ziel
Füge Bean Validation zur Bücher-API hinzu, damit ungültige Daten direkt am API-Eingang abgefangen werden.

## Ausgangslage
Das Projekt in `starter/` enthält die Bücher-API aus Übung 2 — jedoch **ohne Validierung**. Jeder Request wird akzeptiert, auch mit leeren Feldern oder negativen Preisen.

## Aufgaben

### 1. Standard-Constraints auf Book-Record setzen
Öffne `model/Book.java` und füge folgende Constraints hinzu:
- `title`: `@NotBlank`, `@Size(max = 200)`
- `author`: `@NotBlank`
- `isbn`: `@NotBlank`
- `price`: `@Positive`

### 2. @Valid im BookResource ergänzen
Öffne `resource/BookResource.java` und füge `@Valid` vor dem `Book`-Parameter in den Methoden `create()` und `update()` hinzu.

### 3. Custom Constraint @ValidISBN erstellen
Erstelle im Package `constraint/`:
- `ValidISBN.java` — Constraint-Annotation mit `@Constraint(validatedBy = ValidISBNValidator.class)`
- `ValidISBNValidator.java` — Validator der prüft, ob die ISBN dem Format `978-XXXXXXXXXX` entspricht

Füge `@ValidISBN` zum `isbn`-Feld in `Book.java` hinzu.

### 4. Testen

**Build und Deploy:**
```bash
cd exercises/ex03-validation/starter
mvn clean package
mvn wildfly:deploy
```

**Gültiger Request:**
```bash
curl -s -X POST http://localhost:8080/ex03-validation/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Domain-Driven Design",
    "author": "Eric Evans",
    "isbn": "978-0321125217",
    "price": 54.99
  }' | python3 -m json.tool
```

**Ungültiger Request — leere Felder, negativer Preis:**
```bash
curl -s -X POST http://localhost:8080/ex03-validation/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "author": "",
    "isbn": "INVALID",
    "price": -5
  }'
```
Erwartung: HTTP 400 mit Validierungsfehlern.

**Ungültige ISBN:**
```bash
curl -s -X POST http://localhost:8080/ex03-validation/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Book",
    "author": "Test Author",
    "isbn": "123-INVALID",
    "price": 19.99
  }'
```
Erwartung: HTTP 400 mit Fehlermeldung zur ISBN.

## Lösung
Die fertige Lösung findest du in `solution/`.

## Tipps
- Hibernate Validator ist in WildFly bereits enthalten — keine zusätzliche Dependency nötig
- Importiere `jakarta.validation.constraints.*` für Standard-Constraints
- Importiere `jakarta.validation.Valid` für die `@Valid`-Annotation
- Bei Custom Constraints sind `message()`, `groups()` und `payload()` Pflicht-Attribute
- Im Validator: `null`-Werte nicht prüfen — `@NotBlank` kümmert sich um null
