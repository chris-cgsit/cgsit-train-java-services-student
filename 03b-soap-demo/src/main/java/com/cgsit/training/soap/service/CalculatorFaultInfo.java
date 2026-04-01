package com.cgsit.training.soap.service;

/**
 * Fault detail object — transported inside the SOAP Fault <detail> element.
 *
 * Contains structured error information that the client can process
 * programmatically (error code, operation name) instead of just a message string.
 */
public class CalculatorFaultInfo {

    private String operation;
    private String errorCode;

    public CalculatorFaultInfo() {
    }

    public CalculatorFaultInfo(String operation, String errorCode) {
        this.operation = operation;
        this.errorCode = errorCode;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
