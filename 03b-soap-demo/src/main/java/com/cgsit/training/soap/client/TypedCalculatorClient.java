package com.cgsit.training.soap.client;

import com.cgsit.training.soap.generated.CalculatorService;
import com.cgsit.training.soap.generated.CalculatorService_Service;

/**
 * Typsicherer SOAP Client — nutzt die aus der WSDL generierten Klassen.
 *
 * Voraussetzung:
 *   1. mvn generate-sources   (generiert Client-Klassen aus WSDL)
 *   2. WildFly läuft mit cdi-demo deployed
 *
 * Ausführen in IntelliJ: Rechtsklick auf main() → Run
 *
 * Die generierten Klassen liegen in:
 *   target/generated-sources/wsimport/com.cgsit.training.soap.generated/
 *
 * Generierte Klassen:
 *   CalculatorService          — das Interface (Port)
 *   CalculatorService_Service  — die Factory (erzeugt den Port)
 *   Add, AddResponse           — Request/Response Objekte für add()
 *   Multiply, MultiplyResponse — Request/Response Objekte für multiply()
 *   Divide, DivideResponse     — Request/Response Objekte für divide()
 *   ObjectFactory              — Factory für die XML-Objekte
 */
public class TypedCalculatorClient {

    public static void main(String[] args) {
        System.out.println("=== Typsicherer SOAP Client (aus WSDL generiert) ===");
        System.out.println();

        // 1. Factory erzeugen — kennt die WSDL-URL
        CalculatorService_Service factory = new CalculatorService_Service();

        // 2. Port holen — das ist der Proxy zum Server
        CalculatorService calculator = factory.getCalculatorServicePort();

        // 3. Aufrufen — typsicher, mit Autovervollständigung in der IDE!
        int sum = calculator.add(10, 20);
        System.out.println("  add(10, 20)      = " + sum);        // → 30

        int product = calculator.multiply(6, 7);
        System.out.println("  multiply(6, 7)   = " + product);    // → 42

        double quotient = calculator.divide(100, 3);
        System.out.println("  divide(100, 3)   = " + quotient);   // → 33.33...

        System.out.println();
        System.out.println("=== Fertig ===");
        System.out.println();
        System.out.println("Beachte: Der Code sieht aus wie ein lokaler Methodenaufruf.");
        System.out.println("Im Hintergrund wird SOAP XML über HTTP gesendet — komplett abstrahiert.");
    }
}
