package com.cgsit.training.soap.client;

import com.cgsit.training.soap.generated.CalculatorFault;
import com.cgsit.training.soap.generated.CalculatorService;
import com.cgsit.training.soap.generated.CalculatorService_Service;

/**
 * Typsicherer SOAP Client — nutzt die aus der WSDL generierten Klassen.
 *
 * Voraussetzung:
 *   1. mvn generate-sources   (generiert Client-Klassen aus WSDL)
 *   2. WildFly läuft mit soap-demo deployed
 *
 * Ausführen in IntelliJ: Rechtsklick auf main() → Run
 *
 * Generierte Klassen:
 *   CalculatorService          — das Interface (Port)
 *   CalculatorService_Service  — die Factory (erzeugt den Port)
 *   Add, AddResponse           — Request/Response Objekte für add()
 *   Multiply, MultiplyResponse — Request/Response Objekte für multiply()
 *   Divide, DivideResponse     — Request/Response Objekte für divide()
 *   CalculatorFault            — Typed Exception für Fehlerfälle
 *   CalculatorFaultInfo        — Detail-Objekt im Fault (operation, errorCode)
 */
public class TypedCalculatorClient {

    public static void main(String[] args) throws CalculatorFault {
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

        // Division by zero — the server throws CalculatorException (@WebFault)
        // which arrives as a typed CalculatorFault with getFaultInfo()
        System.out.println();
        System.out.println("--- Division by zero (typed fault) ---");
        try {
            calculator.divide(42, 0);
        } catch (CalculatorFault e) {
            var fault = e.getFaultInfo();
            System.out.println("  Caught: " + e.getMessage());
            System.out.println("  Operation: " + fault.getOperation());
            System.out.println("  ErrorCode: " + fault.getErrorCode());
        }

        System.out.println();
        System.out.println("=== Fertig ===");
    }
}
