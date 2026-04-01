# Übung 4 — Fehlerbehandlung & ExceptionMapper

## Ziel
Implementiere eine konsistente Fehlerbehandlung für die Bücher-API mit Custom Exceptions, ExceptionMappern und einer einheitlichen JSON-Fehlerstruktur.

## Ausgangslage
Das Projekt in `starter/` enthält die validierte Bücher-API aus Übung 3. Fehler werden aber noch nicht sauber behandelt — bei einer nicht gefundenen Ressource kommt eine leere 404-Response ohne JSON-Body.

## Aufgaben

### 1. Custom Exceptions erstellen
Erstelle im Package `exception/`:
- `ServiceException.java` — abstrakte Basis-Exception (RuntimeException) mit `statusCode`
- `BookNotFoundException.java` — extends ServiceException, Status 404
- `DuplicateBookException.java` — extends ServiceException, Status 409

### 2. ErrorResponse Record
Erstelle im Package `mapper/`:
- `ErrorResponse.java` — Record mit `status`, `title`, `detail`, `timestamp`

### 3. ExceptionMapper implementieren
Erstelle im Package `mapper/`:
- `ServiceExceptionMapper.java` — `@Provider`, mappt `ServiceException` auf den jeweiligen HTTP-Status
- `ConstraintViolationMapper.java` — `@Provider`, mappt `ConstraintViolationException` auf 400 mit Feldfehlern
- `CatchAllExceptionMapper.java` — `@Provider`, mappt `Exception` auf 500, loggt den Fehler

### 4. BookService anpassen
Ändere den `BookService` so, dass er Exceptions wirft statt Optional/boolean zurückzugeben:
- `findById()` wirft `BookNotFoundException` wenn nicht gefunden
- `create()` wirft `DuplicateBookException` wenn ISBN schon existiert
- `update()` wirft `BookNotFoundException` wenn nicht gefunden
- `delete()` wirft `BookNotFoundException` wenn nicht gefunden

### 5. BookResource vereinfachen
Passe den `BookResource` an — die Fehlerbehandlung übernimmt jetzt der ExceptionMapper.

### 6. Testen

**Build und Deploy:**
```bash
cd exercises/ex04-error-handling/starter
mvn clean package
mvn wildfly:deploy
```

**404 — Buch nicht gefunden:**
```bash
curl -s http://localhost:8080/ex04-error-handling/api/books/999 | python3 -m json.tool
```
Erwartung:
```json
{
    "status": 404,
    "title": "Not Found",
    "detail": "Book with id 999 not found",
    "timestamp": "2026-03-28T10:15:30Z"
}
```

**409 — Duplikat (gleiche ISBN):**
```bash
curl -s -X POST http://localhost:8080/ex04-error-handling/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code Copy",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "price": 29.99
  }' | python3 -m json.tool
```
Erwartung:
```json
{
    "status": 409,
    "title": "Conflict",
    "detail": "Book with ISBN '978-0132350884' already exists",
    "timestamp": "2026-03-28T10:16:00Z"
}
```

**400 — Validierungsfehler:**
```bash
curl -s -X POST http://localhost:8080/ex04-error-handling/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "author": "",
    "isbn": "INVALID",
    "price": -5
  }' | python3 -m json.tool
```
Erwartung: HTTP 400 mit Liste der Validierungsfehler pro Feld.

**204 — Erfolgreiches Löschen:**
```bash
curl -s -o /dev/null -w "%{http_code}" -X DELETE \
  http://localhost:8080/ex04-error-handling/api/books/1
```
Erwartung: `204`

**404 — Nochmal löschen:**
```bash
curl -s -X DELETE http://localhost:8080/ex04-error-handling/api/books/1 | python3 -m json.tool
```
Erwartung: HTTP 404 mit JSON-Fehlermeldung.

## Lösung
Die fertige Lösung findest du in `solution/`.

## Tipps
- ExceptionMapper werden mit `@Provider` beim JAX-RS Runtime registriert
- JAX-RS wählt automatisch den spezifischsten Mapper für die Exception
- Im Catch-All Mapper: Fehler loggen, aber keine Details an den Client senden
- Business Exceptions als Unchecked (RuntimeException) — kein `throws` nötig
