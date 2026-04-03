# WildFly CLI Demo — Cheat Sheet

Alle Pfade gehen davon aus:
- `JBOSS_HOME` = `C:\wildfly-39.0.1.Final`
- Projekt = `C:\work\git\cgsit-train-java-services` (Master) oder Student-Repo

## 1. Offline Setup (WildFly muss NICHT laufen)

```bash
# standalone.xml mit MicroProfile + Datasource + Logging konfigurieren
%JBOSS_HOME%\bin\jboss-cli.bat --file=C:\work\git\cgsit-train-java-services\scripts\wildfly\setup-dev-standalone.cli
```

## 2. WildFly starten

```bash
%JBOSS_HOME%\bin\standalone.bat
```

Pruefen:
- http://localhost:8080/ (Welcome Page)
- http://localhost:8080/health (MicroProfile Health)
- http://localhost:8080/openapi (MicroProfile OpenAPI)

## 3. WAR bauen

```bash
cd C:\work\git\cgsit-train-java-services\final-project\starter
mvn clean package
```

## 4. WAR deployen via CLI

```bash
# Interaktiv
%JBOSS_HOME%\bin\jboss-cli.bat -c

# Im CLI:
deploy C:\work\git\cgsit-train-java-services\final-project\starter\target\final-project.war
ls /deployment
/deployment=final-project.war:read-attribute(name=status)
```

Oder als Einzeiler:
```bash
%JBOSS_HOME%\bin\jboss-cli.bat -c --command="deploy C:\work\git\cgsit-train-java-services\final-project\starter\target\final-project.war --force"
```

## 5. CLI Live-Befehle zeigen

```bash
%JBOSS_HOME%\bin\jboss-cli.bat -c

# Server-Status
:read-attribute(name=server-state)

# Deployments auflisten
ls /deployment

# Datasource testen
/subsystem=datasources/data-source=TrainingDS:test-connection-in-pool()

# Logging-Level live aendern (ohne Restart!)
/subsystem=logging/logger=com.cgsit.training:write-attribute(name=level, value=INFO)
/subsystem=logging/logger=com.cgsit.training:write-attribute(name=level, value=DEBUG)

# Subsysteme auflisten
ls /subsystem

# Undeploy
undeploy final-project.war
```

## 6. Redeploy (nach Code-Aenderung)

```bash
cd C:\work\git\cgsit-train-java-services\final-project\starter
mvn clean package

%JBOSS_HOME%\bin\jboss-cli.bat -c --command="deploy C:\work\git\cgsit-train-java-services\final-project\starter\target\final-project.war --force"
```

## Reihenfolge fuer die Demo

1. `setup-dev-standalone.cli` ausfuehren (full path!) → zeigt embed-server Konzept
2. WildFly starten → /health und /openapi pruefen
3. WAR bauen → `mvn clean package`
4. CLI interaktiv → `deploy` (full path), `ls /deployment`, Status pruefen
5. App testen → http://localhost:8080/final-project/api/customers
6. Logging live aendern → zeigt dass kein Restart noetig
7. Undeploy → `undeploy final-project.war`
