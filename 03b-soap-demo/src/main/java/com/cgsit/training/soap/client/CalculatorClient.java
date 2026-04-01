package com.cgsit.training.soap.client;

import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Standalone SOAP Client — ruft den CalculatorService auf.
 *
 * Voraussetzung: WildFly läuft und cdi-demo ist deployed.
 *
 * Ausführen in IntelliJ: Rechtsklick auf main() → Run
 *
 * Dieser Client sendet rohe SOAP XML Requests per HTTP POST.
 * In der Praxis würde man wsimport nutzen um typsichere Client-Klassen
 * zu generieren — hier zeigen wir den Mechanismus direkt.
 */
public class CalculatorClient {

    private static final String ENDPOINT = "http://localhost:8080/soap-demo/CalculatorService";
    private static final String NAMESPACE = "http://soap.training.cgsit.com/";

    public static void main(String[] args) throws Exception {
        System.out.println("=== SOAP Client Demo ===");
        System.out.println("Endpoint: " + ENDPOINT);
        System.out.println("WSDL:     " + ENDPOINT + "?wsdl");
        System.out.println();

        // Addition: 10 + 20
        callAndPrint("add", "<a>10</a><b>20</b>", "10 + 20");

        // Multiplikation: 6 × 7
        callAndPrint("multiply", "<a>6</a><b>7</b>", "6 × 7");

        // Division: 100 / 3
        callAndPrint("divide", "<a>100</a><b>3</b>", "100 / 3");

        System.out.println("=== SOAP Client fertig ===");
    }

    private static void callAndPrint(String operation, String params, String label) throws Exception {
        String response = sendSoapRequest(operation, params);
        String result = extractResult(response);
        System.out.println("  " + label + " = " + result);
    }

    /**
     * Sendet einen SOAP Request per HTTP POST.
     * Das ist im Kern was jeder SOAP Client macht — eine XML Nachricht
     * in einem HTTP POST Body an den Endpoint senden.
     */
    private static String sendSoapRequest(String operation, String params) throws Exception {
        String soapXml = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:calc="%s">
                    <soap:Body>
                        <calc:%s>%s</calc:%s>
                    </soap:Body>
                </soap:Envelope>
                """.formatted(NAMESPACE, operation, params, operation);

        var conn = (HttpURLConnection) URI.create(ENDPOINT).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setDoOutput(true);

        try (var out = conn.getOutputStream()) {
            out.write(soapXml.getBytes());
        }

        try (var in = conn.getInputStream()) {
            return new String(in.readAllBytes());
        }
    }

    /**
     * Extrahiert den <return>-Wert aus dem SOAP Response XML.
     * In der Praxis würde der generierte Client das automatisch machen.
     */
    private static String extractResult(String xml) {
        int start = xml.indexOf("<return>") + 8;
        int end = xml.indexOf("</return>");
        if (start > 7 && end > start) {
            return xml.substring(start, end);
        }
        return "(nicht gefunden in: " + xml + ")";
    }
}
