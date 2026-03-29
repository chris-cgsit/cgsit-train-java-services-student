# Java Services Training — Übungen

Dieses Repository enthält die Übungen für den Java Services Kurs.

## Setup

```bash
# Repository klonen
git clone https://github.com/chris-cgsit/cgsit-train-java-services-student.git
cd cgsit-train-java-services-student

# Vor jeder Übung: neues Material holen
git pull
```

## Voraussetzungen

- OpenJDK **21**
- Maven **3.9+**
- WildFly **39.0.1** (lokal installiert, `JBOSS_HOME` gesetzt)
- IntelliJ IDEA oder Eclipse
- Git

## Übung starten

```bash
# In den Übungsordner wechseln
cd ex01-rest-crud

# Bauen und auf WildFly deployen
mvn clean package wildfly:dev

# Testen
curl http://localhost:8080/ex01-rest-crud/api/books | jq
```

## Übungen

| # | Übung | Tag | Thema |
|---|-------|-----|-------|
| 1 | ex01-rest-crud | Tag 1 | REST CRUD API (Bücher) |
| 2 | ex02-cdi-refactoring | Tag 2 | CDI Refactoring |
| 3 | ex03-validation | Tag 3 | Bean Validation |
| 4 | ex04-error-handling | Tag 3 | Fehlerbehandlung |
| 5 | ex05-testing | Tag 4 | Unit Tests |
| 6 | ex06-integration | Tag 5 | Integrationsprojekt |

Neue Übungen und Musterlösungen werden vom Trainer per `git push` bereitgestellt.
