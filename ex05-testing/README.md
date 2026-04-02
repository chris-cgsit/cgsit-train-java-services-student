# Uebung 5 -- Testing

## Ziel

Schreibe Unit Tests fuer den `BookService` mit JUnit 5 und Mockito.
Dabei wird der `BookStore` gemockt, um die Service-Logik isoliert zu testen.

## Voraussetzungen

- Java 21+
- Maven 3.9+
- Das Projekt aus Uebung 4 (Book API) dient als Grundlage

## Aufgaben

### 1. Imports ergaenzen

Oeffne `src/test/java/com/cgsit/training/bookapi/unit/BookServiceTest.java` und ergaenze die auskommentierten Imports.

### 2. Fuenf Unit Tests schreiben

Implementiere die folgenden Testfaelle:

| # | Testfall | Erwartetes Verhalten |
|---|----------|---------------------|
| 1 | `findById` -- Buch gefunden | `store.findById(1L)` gibt `Optional.of(book)` zurueck, Service gibt das Buch zurueck |
| 2 | `findById` -- nicht gefunden | `store.findById(99L)` gibt `Optional.empty()` zurueck, Service wirft `BookNotFoundException` |
| 3 | `create` -- gueltiges Buch | `store.existsByIsbn()` gibt `false` zurueck, Buch wird gespeichert |
| 4 | `create` -- doppelte ISBN | `store.existsByIsbn()` gibt `true` zurueck, Service wirft `DuplicateBookException` |
| 5 | `delete` -- nicht gefunden | `store.delete(99L)` gibt `false` zurueck, Service wirft `BookNotFoundException` |

### 3. Mockito-Methoden verwenden

- `when(mock.methode()).thenReturn(wert)` -- Mock-Verhalten definieren
- `verify(mock).methode()` -- Aufruf pruefen
- `verify(mock, never()).methode()` -- Nicht-Aufruf pruefen

### 4. Assertions verwenden

- `assertEquals(expected, actual)` -- Gleichheit pruefen
- `assertThrows(Exception.class, () -> ...)` -- Exception pruefen
- `assertAll(() -> ..., () -> ...)` -- Mehrere Pruefungen zusammen

## Tests ausfuehren

```bash
mvn test
```

Erwartete Ausgabe:

```
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Tipps

- Nutze `@DisplayName` fuer lesbare Testnamen
- Nutze `@Nested` um Tests zu gruppieren (z.B. nach Methode)
- Erstelle Testdaten als lokale Variablen im jeweiligen Test
- Die Test-Klasse braucht `@ExtendWith(MockitoExtension.class)`
- `@Mock` erstellt ein Mock-Objekt, `@InjectMocks` injiziert die Mocks

## Projektstruktur

```
ex05-testing/
  starter/          <-- Hier arbeiten
    src/
      main/java/    <-- Book API (vollstaendig)
      test/java/    <-- Tests (TODO)
    pom.xml
  solution/         <-- Musterloesung
```

## Loesung

Die Musterloesung findest du im `solution/` Verzeichnis.
