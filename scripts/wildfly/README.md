# WildFly CLI Scripts

Wiederholbare, versionierte Konfiguration für WildFly — Konfiguration als Code.

## Struktur

```
scripts/wildfly/
  ├── 00-common.cli           ← Basis (MicroProfile, Logging)
  ├── 01-datasource-dev.cli   ← H2 In-Memory
  ├── 01-datasource-prod.cli  ← PostgreSQL mit Connection Pool
  ├── 02-logging-dev.cli      ← DEBUG (Hibernate SQL, RESTEasy)
  ├── 02-logging-prod.cli     ← WARN Console + Rotating File
  ├── setup-dev.cli           ← Alles zusammen: DEV
  └── setup-prod.cli          ← Alles zusammen: PROD
```

## Verwendung

### DEV Setup (offline — WildFly muss nicht laufen)

```bash
%JBOSS_HOME%\bin\jboss-cli.bat --file=scripts/wildfly/setup-dev.cli
```

Danach WildFly normal starten — alle Konfigurationen sind in `standalone.xml` gespeichert.

### PROD Setup

```bash
# Environment Variablen setzen
set DB_HOST=prod-db
set DB_PORT=5432
set DB_NAME=training
set DB_USER=admin
set DB_PASSWORD=secret

# Konfiguration anwenden
%JBOSS_HOME%\bin\jboss-cli.bat --file=scripts/wildfly/setup-prod.cli
```

### Einzelne Scripts (auf laufendem Server)

```bash
%JBOSS_HOME%\bin\jboss-cli.bat --connect --file=scripts/wildfly/02-logging-dev.cli
```

## Was wird konfiguriert?

| Script | Konfiguriert |
|--------|-------------|
| `00-common.cli` | MicroProfile Config, Health, OpenAPI Subsysteme |
| `01-datasource-dev.cli` | H2 In-Memory Datasource (`TrainingDS`) |
| `01-datasource-prod.cli` | PostgreSQL mit Pool (5-30 Connections) |
| `02-logging-dev.cli` | DEBUG für App, Hibernate SQL, RESTEasy |
| `02-logging-prod.cli` | WARN Console, tägliche Log-Rotation |
