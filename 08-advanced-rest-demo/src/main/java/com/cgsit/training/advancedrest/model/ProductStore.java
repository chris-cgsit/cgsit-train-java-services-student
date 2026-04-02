package com.cgsit.training.advancedrest.model;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ProductStore {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @PostConstruct
    void init() {
        // 25 sample products for meaningful pagination
        save(new Product(0, "Mechanical Keyboard", new BigDecimal("89.99"), "Peripherie"));
        save(new Product(0, "USB-C Hub", new BigDecimal("34.50"), "Electronics"));
        save(new Product(0, "IntelliJ IDEA License", new BigDecimal("499.00"), "Software"));
        save(new Product(0, "Clean Code", new BigDecimal("34.99"), "Books"));
        save(new Product(0, "27\" Monitor", new BigDecimal("349.00"), "Electronics"));
        save(new Product(0, "Ergonomic Mouse", new BigDecimal("49.99"), "Peripherie"));
        save(new Product(0, "Standing Desk", new BigDecimal("599.00"), "Furniture"));
        save(new Product(0, "Webcam HD", new BigDecimal("79.99"), "Electronics"));
        save(new Product(0, "Headset Pro", new BigDecimal("129.00"), "Peripherie"));
        save(new Product(0, "Design Patterns", new BigDecimal("44.99"), "Books"));
        save(new Product(0, "USB Microphone", new BigDecimal("89.00"), "Electronics"));
        save(new Product(0, "Desk Lamp LED", new BigDecimal("39.99"), "Furniture"));
        save(new Product(0, "Refactoring", new BigDecimal("47.99"), "Books"));
        save(new Product(0, "Wireless Keyboard", new BigDecimal("69.99"), "Peripherie"));
        save(new Product(0, "Laptop Stand", new BigDecimal("29.99"), "Furniture"));
        save(new Product(0, "Docker Desktop License", new BigDecimal("60.00"), "Software"));
        save(new Product(0, "Cable Management Kit", new BigDecimal("19.99"), "Furniture"));
        save(new Product(0, "4K Monitor", new BigDecimal("449.00"), "Electronics"));
        save(new Product(0, "Effective Java", new BigDecimal("39.99"), "Books"));
        save(new Product(0, "Gaming Mouse", new BigDecimal("59.99"), "Peripherie"));
        save(new Product(0, "Monitor Arm", new BigDecimal("89.00"), "Furniture"));
        save(new Product(0, "JetBrains All Products", new BigDecimal("649.00"), "Software"));
        save(new Product(0, "Thunderbolt Dock", new BigDecimal("199.00"), "Electronics"));
        save(new Product(0, "Java Concurrency in Practice", new BigDecimal("42.99"), "Books"));
        save(new Product(0, "Split Keyboard", new BigDecimal("159.00"), "Peripherie"));
    }

    public List<Product> findAll() {
        return List.copyOf(products);
    }

    public Optional<Product> findById(long id) {
        return products.stream().filter(p -> p.id() == id).findFirst();
    }

    /**
     * Search with filtering and sorting — paging is applied by the caller.
     */
    public List<Product> search(String name, String category,
                                 String sortBy, SortDirection sortDirection) {
        var stream = products.stream();

        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            stream = stream.filter(p -> p.name().toLowerCase().contains(lower));
        }
        if (category != null && !category.isBlank()) {
            stream = stream.filter(p -> category.equalsIgnoreCase(p.category()));
        }

        Comparator<Product> comparator = switch (sortBy != null ? sortBy : "id") {
            case "name" -> Comparator.comparing(Product::name, String.CASE_INSENSITIVE_ORDER);
            case "price" -> Comparator.comparing(Product::price);
            case "category" -> Comparator.comparing(Product::category, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparingLong(Product::id);
        };

        if (sortDirection == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        return stream.sorted(comparator).toList();
    }

    public Product save(Product product) {
        Product created = new Product(idCounter.incrementAndGet(),
                product.name(), product.price(), product.category());
        products.add(created);
        return created;
    }
}
