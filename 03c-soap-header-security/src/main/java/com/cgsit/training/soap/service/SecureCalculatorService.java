package com.cgsit.training.soap.service;

import jakarta.annotation.Resource;
import jakarta.jws.HandlerChain;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.WebServiceException;

/**
 * SOAP Web Service with header parameters, handler chain, and role-based access.
 *
 * Security flow:
 *   1. Client sends apiKey + correlationId in SOAP Header
 *   2. LoggingHandler logs the raw SOAP XML
 *   3. AuthHandler validates the apiKey and sets "auth.role" in MessageContext
 *   4. This service reads the role via WebServiceContext
 *
 * This is the same pattern as Keycloak/OAuth2 integration:
 *   - Handler validates token → sets security context
 *   - Service queries security context → no auth logic in business code
 *
 * After deploy, the WSDL is available at:
 *   http://localhost:8080/soap-header-security/SecureCalculatorService?wsdl
 */
@WebService(serviceName = "SecureCalculatorService",
            targetNamespace = "http://soap.training.cgsit.com/")
@HandlerChain(file = "/handler-chain.xml")
public class SecureCalculatorService {

    // Injected by the JAX-WS runtime — provides access to the MessageContext
    // set by the handler chain (similar to SecurityContext in REST/Servlet).
    @Resource
    WebServiceContext wsContext;

    @WebMethod
    public int add(
            @WebParam(name = "apiKey", header = true) String apiKey,
            @WebParam(name = "correlationId", header = true) String correlationId,
            @WebParam(name = "a") int a,
            @WebParam(name = "b") int b) {
        logWithRole(correlationId, "add", a + " + " + b);
        return a + b;
    }

    @WebMethod
    public int multiply(
            @WebParam(name = "apiKey", header = true) String apiKey,
            @WebParam(name = "correlationId", header = true) String correlationId,
            @WebParam(name = "a") int a,
            @WebParam(name = "b") int b) {
        logWithRole(correlationId, "multiply", a + " * " + b);
        return a * b;
    }

    @WebMethod
    public double divide(
            @WebParam(name = "apiKey", header = true) String apiKey,
            @WebParam(name = "correlationId", header = true) String correlationId,
            @WebParam(name = "a") double a,
            @WebParam(name = "b") double b) {
        requireRole("admin", "divide");
        if (b == 0) {
            throw new WebServiceException("Division by zero");
        }
        logWithRole(correlationId, "divide", a + " / " + b);
        return a / b;
    }

    /**
     * Read the role set by AuthHandler from the MessageContext.
     * Returns null if no role was set (should not happen — AuthHandler rejects first).
     */
    private String getRole() {
        return (String) wsContext.getMessageContext().get("auth.role");
    }

    /**
     * Enforce a required role — throws WebServiceException if not matched.
     * Example: divide() requires "admin" role.
     */
    private void requireRole(String requiredRole, String operation) {
        String role = getRole();
        if (!requiredRole.equals(role)) {
            throw new WebServiceException(
                    "Access denied: operation '" + operation + "' requires role '"
                    + requiredRole + "', but current role is '" + role + "'");
        }
    }

    private void logWithRole(String correlationId, String operation, String expression) {
        System.out.println("[SecureCalculator] correlationId=" + correlationId
                + " role=" + getRole()
                + " operation=" + operation + " expr=" + expression);
    }
}
