# Übung 1 — REST CRUD API (Bücher)

## Ziel
Erstelle eine REST API für die Verwaltung von **Büchern** mit JAX-RS.

## Was ist vorgegeben?
- `pom.xml` — fertig konfiguriert (Jakarta EE 10, WildFly Plugin)
- `RestApplication.java` — fertig (`@ApplicationPath("/api")`)
- `Book.java` — fertig (Record mit id, title, author, isbn, price)
- `BookStore.java` — Skelett mit TODOs (ihr implementiert die Methoden)
- `BookResource.java` — Skelett mit TODOs (ihr implementiert die Endpoints)
- `beans.xml` — fertig (CDI aktiviert)

## Aufgaben

### 1. BookStore implementieren
Öffne `data/BookStore.java` und implementiere die TODO-Methoden:
- `init()` — 3–5 Beispiel-Bücher mit `save()` hinzufügen
- `findAll()` — alle Bücher als Liste zurückgeben
- `findById(id)` — Buch nach ID suchen
- `save(book)` — neues Buch mit auto-generierter ID speichern
- `update(id, book)` — bestehendes Buch aktualisieren
- `delete(id)` — Buch löschen

### 2. BookResource implementieren
Öffne `resource/BookResource.java` und implementiere die TODO-Endpoints:

| HTTP | Pfad | Beschreibung | Status-Code |
|------|------|-------------|-------------|
| `GET` | `/api/books` | Alle Bücher | 200 |
| `GET` | `/api/books/{id}` | Ein Buch | 200 oder 404 |
| `POST` | `/api/books` | Neues Buch | 201 |
| `PUT` | `/api/books/{id}` | Aktualisieren | 200 oder 404 |
| `DELETE` | `/api/books/{id}` | Löschen | 204 oder 404 |

## Starten
```bash
cd exercises/ex01-rest-crud/starter
mvn clean package wildfly:dev
```

## Testen mit curl
```bash
# Alle Bücher
curl http://localhost:8080/ex01-rest-crud/api/books | jq

# Ein Buch
curl http://localhost:8080/ex01-rest-crud/api/books/1 | jq

# Neues Buch erstellen
curl -X POST http://localhost:8080/ex01-rest-crud/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"The Pragmatic Programmer","author":"David Thomas","isbn":"978-0135957059","price":49.99}' | jq

# Buch aktualisieren
curl -X PUT http://localhost:8080/ex01-rest-crud/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code (2nd Ed)","author":"Robert C. Martin","isbn":"978-0132350884","price":39.99}' | jq

# Buch löschen
curl -X DELETE http://localhost:8080/ex01-rest-crud/api/books/1 -v
```

## Tipps
- Schaut euch das `rest-api-demo` Beispiel an — die Struktur ist identisch
- `@Inject BookStore store` im Resource nicht vergessen
- `idCounter.incrementAndGet()` gibt die nächste ID
- `List.copyOf()` erstellt eine unveränderliche Kopie
- `Optional.empty()` wenn nichts gefunden

## Zeit
⏱ 60 Minuten
