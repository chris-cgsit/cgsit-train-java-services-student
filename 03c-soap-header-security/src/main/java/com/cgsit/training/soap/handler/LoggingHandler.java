package com.cgsit.training.soap.handler;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import javax.xml.namespace.QName;

/**
 * SOAPHandler that logs inbound and outbound SOAP messages.
 *
 * Handler Chain order: LoggingHandler runs FIRST (before AuthHandler),
 * so we see the raw request even if authentication fails.
 *
 * This is the SOAP equivalent of a Servlet Filter or CDI Interceptor.
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        String direction = outbound ? "OUTBOUND (Response)" : "INBOUND (Request)";

        System.out.println("─── SOAP " + direction + " ───");
        logMessage(context.getMessage());
        System.out.println("─────────────────────────────");

        return true; // continue processing
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        System.out.println("─── SOAP FAULT ───");
        logMessage(context.getMessage());
        System.out.println("──────────────────");
        return true;
    }

    @Override
    public void close(MessageContext context) {
        // nothing to clean up
    }

    @Override
    public Set<QName> getHeaders() {
        // this handler does not process any specific headers
        return Set.of();
    }

    private void logMessage(SOAPMessage message) {
        try {
            var baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            System.out.println(baos.toString(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("  (could not serialize SOAP message: " + e.getMessage() + ")");
        }
    }
}
