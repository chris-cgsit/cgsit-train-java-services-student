package com.cgsit.training.errorhandling.data;

import com.cgsit.training.errorhandling.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ProductStore {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @PostConstruct
    void init() {
        save(new Product(0, "Mechanical Keyboard", new BigDecimal("89.99"), "Peripherie"));
        save(new Product(0, "USB-C Hub", new BigDecimal("34.50"), "Electronics"));
        save(new Product(0, "IntelliJ IDEA License", new BigDecimal("499.00"), "Software"));
        save(new Product(0, "Clean Code", new BigDecimal("34.99"), "Books"));
        save(new Product(0, "27\" Monitor", new BigDecimal("349.00"), "Electronics"));
    }

    public List<Product> findAll() {
        return List.copyOf(products);
    }

    public Optional<Product> findById(long id) {
        return products.stream()
                .filter(p -> p.id() == id)
                .findFirst();
    }

    public boolean existsByName(String name) {
        return products.stream()
                .anyMatch(p -> p.name().equalsIgnoreCase(name));
    }

    public Product save(Product product) {
        Product created = new Product(
                idCounter.incrementAndGet(),
                product.name(),
                product.price(),
                product.category()
        );
        products.add(created);
        return created;
    }

    public Optional<Product> update(long id, Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).id() == id) {
                Product updated = new Product(id, product.name(), product.price(), product.category());
                products.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public boolean delete(long id) {
        return products.removeIf(p -> p.id() == id);
    }
}
