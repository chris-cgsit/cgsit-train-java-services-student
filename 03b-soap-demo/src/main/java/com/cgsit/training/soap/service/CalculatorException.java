package com.cgsit.training.soap.service;

import jakarta.xml.ws.WebFault;

/**
 * Custom SOAP Fault exception.
 *
 * @WebFault makes this a proper SOAP Fault:
 *   - Appears as <fault> element in the WSDL
 *   - Client receives structured CalculatorFaultInfo (not just a string)
 *   - Generated client code gets a typed exception to catch
 *
 * Without @WebFault, JAX-WS wraps any exception as a generic SOAP Fault
 * with just a message — the client cannot distinguish error types.
 */
@WebFault(name = "CalculatorFault",
          targetNamespace = "http://soap.training.cgsit.com/")
public class CalculatorException extends Exception {

    private final CalculatorFaultInfo faultInfo;

    public CalculatorException(String message, CalculatorFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public CalculatorException(String message, CalculatorFaultInfo faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * Required by JAX-WS — the runtime calls this to serialize the fault detail.
     */
    public CalculatorFaultInfo getFaultInfo() {
        return faultInfo;
    }
}
