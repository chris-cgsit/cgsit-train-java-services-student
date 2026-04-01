package com.cgsit.training.soap.handler;

import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;

/**
 * SOAPHandler that validates the API key from the SOAP header
 * and sets the user role in the MessageContext.
 *
 * Runs AFTER LoggingHandler (handler-chain.xml order matters).
 *
 * This is the same pattern a Keycloak handler would use:
 *   1. Extract token/key from SOAP or HTTP header
 *   2. Validate against auth server (here: hardcoded set, Keycloak: token introspection/JWKS)
 *   3. Set role in MessageContext → service reads it via WebServiceContext
 *
 * Valid API keys and their roles:
 *   - "admin-key-2025"    → role "admin"
 *   - "training-key-2025" → role "user"
 *
 * The service reads the role via:
 *   wsContext.getMessageContext().get("auth.role")
 */
public class AuthHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Map<String, String> API_KEY_ROLES = Map.of(
            "admin-key-2025", "admin",
            "training-key-2025", "user"
    );

    private static final String NAMESPACE = "http://soap.training.cgsit.com/";

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            return true; // only check inbound requests
        }

        String apiKey = extractApiKey(context);

        if (apiKey == null || apiKey.isBlank()) {
            throw new jakarta.xml.ws.soap.SOAPFaultException(
                    createFault(context, "Missing API key in SOAP header"));
        }

        String role = API_KEY_ROLES.get(apiKey);
        if (role == null) {
            throw new jakarta.xml.ws.soap.SOAPFaultException(
                    createFault(context, "Invalid API key: " + apiKey));
        }

        // Set role in MessageContext — the service reads this via WebServiceContext.
        // Scope.APPLICATION makes it visible to the service endpoint.
        context.put("auth.role", role);
        context.setScope("auth.role", MessageContext.Scope.APPLICATION);

        System.out.println("[AuthHandler] API key accepted: " + apiKey + " → role=" + role);
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
        // declare which headers this handler understands
        return Set.of(new QName(NAMESPACE, "apiKey"));
    }

    private String extractApiKey(SOAPMessageContext context) {
        try {
            SOAPHeader header = context.getMessage().getSOAPHeader();
            if (header == null) {
                return null;
            }
            Iterator<?> elements = header.getChildElements(new QName(NAMESPACE, "apiKey"));
            if (elements.hasNext()) {
                return ((jakarta.xml.soap.SOAPElement) elements.next()).getTextContent();
            }
        } catch (SOAPException e) {
            System.out.println("[AuthHandler] Error reading header: " + e.getMessage());
        }
        return null;
    }

    private jakarta.xml.soap.SOAPFault createFault(SOAPMessageContext context, String message) {
        try {
            var factory = jakarta.xml.soap.SOAPFactory.newInstance();
            return factory.createFault(message, new QName(
                    "http://schemas.xmlsoap.org/soap/envelope/", "Client"));
        } catch (SOAPException e) {
            throw new RuntimeException("Could not create SOAPFault", e);
        }
    }
}
