package com.cgsit.training.soap.client;

import com.cgsit.training.soap.generated.SecureCalculatorService;
import com.cgsit.training.soap.generated.SecureCalculatorService_Service;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.xml.namespace.QName;

/**
 * Typed SOAP Client — uses generated classes + adds headers programmatically.
 *
 * The generated client from WSDL gives us type-safe method calls.
 * But for SOAP headers we need a client-side SOAPHandler that
 * injects apiKey and correlationId into the outbound message.
 *
 * Prerequisite:
 *   1. mvn generate-sources
 *   2. WildFly running with soap-header-security deployed
 */
public class TypedSecureClient {

    private static final String API_KEY = "training-key-2025";
    private static final String NAMESPACE = "http://soap.training.cgsit.com/";

    public static void main(String[] args) {
        System.out.println("=== Typed Secure SOAP Client ===");
        System.out.println();

        // 1. Create service factory from WSDL
        SecureCalculatorService_Service factory = new SecureCalculatorService_Service();

        // 2. Get the port (proxy to the server)
        SecureCalculatorService calculator = factory.getSecureCalculatorServicePort();

        // 3. Add client-side handler for headers
        var binding = ((BindingProvider) calculator).getBinding();
        @SuppressWarnings("rawtypes")
        List<Handler> chain = binding.getHandlerChain();
        chain.add(new ClientHeaderHandler(API_KEY));
        binding.setHandlerChain(chain);

        // 4. Call operations — type-safe, headers added automatically
        int sum = calculator.add(10, 20);
        System.out.println("  add(10, 20)      = " + sum);

        int product = calculator.multiply(6, 7);
        System.out.println("  multiply(6, 7)   = " + product);

        // 5. divide() requires "admin" role — "training-key-2025" has role "user"
        //    → server returns SOAP Fault (Access denied)
        System.out.println();
        System.out.println("--- divide with 'user' role (access denied) ---");
        try {
            calculator.divide(100, 3);
        } catch (jakarta.xml.ws.soap.SOAPFaultException e) {
            System.out.println("  SOAP Fault: " + e.getMessage());
        }

        // 6. Switch to admin key and retry
        System.out.println();
        System.out.println("--- divide with 'admin' role ---");
        chain.clear();
        chain.add(new ClientHeaderHandler("admin-key-2025"));
        binding.setHandlerChain(chain);

        double quotient = calculator.divide(100, 3);
        System.out.println("  divide(100, 3)   = " + quotient);

        System.out.println();
        System.out.println("=== Done ===");
    }

    /**
     * Client-side SOAPHandler that injects apiKey and correlationId
     * into every outbound SOAP request header.
     */
    static class ClientHeaderHandler implements SOAPHandler<SOAPMessageContext> {

        private final String apiKey;

        ClientHeaderHandler(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        public boolean handleMessage(SOAPMessageContext context) {
            boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (!outbound) {
                return true; // only modify outbound requests
            }

            try {
                var header = context.getMessage().getSOAPHeader();
                if (header == null) {
                    header = context.getMessage().getSOAPPart().getEnvelope().addHeader();
                }

                // Add apiKey header
                var apiKeyElem = header.addChildElement("apiKey", "tns", NAMESPACE);
                apiKeyElem.setTextContent(apiKey);

                // Add correlationId header (new UUID per request)
                var corrIdElem = header.addChildElement("correlationId", "tns", NAMESPACE);
                corrIdElem.setTextContent(UUID.randomUUID().toString().substring(0, 8));

            } catch (Exception e) {
                throw new RuntimeException("Could not add SOAP headers", e);
            }

            return true;
        }

        @Override
        public boolean handleFault(SOAPMessageContext context) {
            return true;
        }

        @Override
        public void close(MessageContext context) {
        }

        @Override
        public Set<QName> getHeaders() {
            return Set.of();
        }
    }
}
