package com.cgsit.training.soap.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

/**
 * SOAP Web Service — deployed automatically on WildFly.
 *
 * After deploy, the WSDL is available at:
 *   http://localhost:8080/cdi-demo/CalculatorService?wsdl
 *
 * The SOAP endpoint accepts requests at:
 *   http://localhost:8080/cdi-demo/CalculatorService
 *
 * From the WSDL, a Java client can be generated:
 *   wsimport -keep -p com.cgsit.client http://localhost:8080/cdi-demo/CalculatorService?wsdl
 */
@WebService(serviceName = "CalculatorService",
            targetNamespace = "http://soap.training.cgsit.com/")
public class CalculatorService {

    @WebMethod
    public int add(@WebParam(name = "a") int a,
                   @WebParam(name = "b") int b) {
        return a + b;
    }

    @WebMethod
    public int multiply(@WebParam(name = "a") int a,
                        @WebParam(name = "b") int b) {
        return a * b;
    }

    @WebMethod
    public double divide(@WebParam(name = "a") double a,
                         @WebParam(name = "b") double b) throws CalculatorException {
        if (b == 0) {
            throw new CalculatorException("Division by zero",
                    new CalculatorFaultInfo("divide", "DIV_BY_ZERO"));
        }
        return a / b;
    }
}
