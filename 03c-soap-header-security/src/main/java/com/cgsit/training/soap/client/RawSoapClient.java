package com.cgsit.training.soap.client;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.UUID;

/**
 * Raw SOAP Client — sends XML with header parameters.
 *
 * Shows the actual SOAP envelope structure:
 *   - <soap:Header> contains apiKey and correlationId
 *   - <soap:Body> contains the operation
 *
 * Prerequisite: WildFly running with soap-header-security deployed.
 */
public class RawSoapClient {

    private static final String ENDPOINT = "http://localhost:8080/soap-header-security/SecureCalculatorService";
    private static final String NAMESPACE = "http://soap.training.cgsit.com/";
    private static final String API_KEY = "training-key-2025";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Raw SOAP Client with Headers ===");
        System.out.println("Endpoint: " + ENDPOINT);
        System.out.println("API Key:  " + API_KEY);
        System.out.println();

        // Valid request: add 10 + 20
        String corrId1 = UUID.randomUUID().toString().substring(0, 8);
        callAndPrint("add", "<a>10</a><b>20</b>", "10 + 20", API_KEY, corrId1);

        // Valid request: multiply 6 * 7
        String corrId2 = UUID.randomUUID().toString().substring(0, 8);
        callAndPrint("multiply", "<a>6</a><b>7</b>", "6 * 7", API_KEY, corrId2);

        // Invalid API key — should return SOAP Fault
        System.out.println("\n--- Test: Invalid API key ---");
        try {
            callAndPrint("add", "<a>1</a><b>1</b>", "1 + 1", "wrong-key", "test-bad-key");
        } catch (Exception e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        // Missing API key — should return SOAP Fault
        System.out.println("\n--- Test: Missing API key ---");
        try {
            callNoAuth("add", "<a>1</a><b>1</b>", "1 + 1");
        } catch (Exception e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        System.out.println("\n=== Done ===");
    }

    private static void callAndPrint(String operation, String params, String label,
                                     String apiKey, String correlationId) throws Exception {
        String response = sendSoapRequest(operation, params, apiKey, correlationId);
        String result = extractResult(response);
        System.out.println("  [" + correlationId + "] " + label + " = " + result);
    }

    private static void callNoAuth(String operation, String params, String label) throws Exception {
        // SOAP envelope WITHOUT header — triggers auth failure
        String soapXml = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:calc="%s">
                    <soap:Body>
                        <calc:%s>%s</calc:%s>
                    </soap:Body>
                </soap:Envelope>
                """.formatted(NAMESPACE, operation, params, operation);

        String response = sendRaw(soapXml);
        System.out.println("  Response: " + response.substring(0, Math.min(200, response.length())) + "...");
    }

    /**
     * Sends a SOAP request with apiKey and correlationId in the header.
     */
    private static String sendSoapRequest(String operation, String params,
                                          String apiKey, String correlationId) throws Exception {
        String soapXml = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:calc="%s">
                    <soap:Header>
                        <calc:apiKey>%s</calc:apiKey>
                        <calc:correlationId>%s</calc:correlationId>
                    </soap:Header>
                    <soap:Body>
                        <calc:%s>%s</calc:%s>
                    </soap:Body>
                </soap:Envelope>
                """.formatted(NAMESPACE, apiKey, correlationId, operation, params, operation);

        return sendRaw(soapXml);
    }

    private static String sendRaw(String soapXml) throws Exception {
        var conn = (HttpURLConnection) URI.create(ENDPOINT).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setDoOutput(true);

        try (var out = conn.getOutputStream()) {
            out.write(soapXml.getBytes());
        }

        // read response or error stream
        try (var in = conn.getInputStream()) {
            return new String(in.readAllBytes());
        } catch (Exception e) {
            try (var err = conn.getErrorStream()) {
                if (err != null) {
                    return new String(err.readAllBytes());
                }
            }
            throw e;
        }
    }

    private static String extractResult(String xml) {
        int start = xml.indexOf("<return>") + 8;
        int end = xml.indexOf("</return>");
        if (start > 7 && end > start) {
            return xml.substring(start, end);
        }
        return "(not found in: " + xml + ")";
    }
}
