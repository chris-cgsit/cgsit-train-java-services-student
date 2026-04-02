package com.cgsit.training.testing.util;

import com.cgsit.training.testing.model.Product;

public final class TestData {

    private TestData() {
        // Utility class
    }

    public static Product laptop() {
        return new Product(1L, "Laptop", 999.99);
    }

    public static Product maus() {
        return new Product(2L, "Maus", 29.99);
    }

    public static Product monitor() {
        return new Product(3L, "Monitor", 449.00);
    }

    public static Product newProduct(String name, double price) {
        return new Product(null, name, price);
    }

    public static Product invalidProduct() {
        return new Product(null, "", -5.0);
    }
}
