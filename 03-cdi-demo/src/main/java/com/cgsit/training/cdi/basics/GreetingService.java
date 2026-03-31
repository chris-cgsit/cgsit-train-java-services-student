package com.cgsit.training.cdi.basics;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String greet(String name) {
        return "Hallo " + name + "! Willkommen zum CDI-Demo.";
    }
}
