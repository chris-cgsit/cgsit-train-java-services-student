package com.cgsit.training.servicelayer.repository;

import com.cgsit.training.servicelayer.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository layer — responsible ONLY for data access.
 *
 * No business logic here. No HTTP. Just CRUD operations on the data store.
 * In a real application this would use JPA/Hibernate to access a database.
 */
@ApplicationScoped
public class ProductRepository {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @PostConstruct
    void init() {
        save(new Product(0, "Laptop", 999.99, "Electronics"));
        save(new Product(0, "Tastatur", 79.99, "Peripherie"));
        save(new Product(0, "Defektes Teil", -10.00, "Schrott"));
        save(new Product(0, "Monitor 27\"", 349.00, "Electronics"));
    }

    public List<Product> findAll() {
        return List.copyOf(products);
    }

    public Optional<Product> findById(long id) {
        return products.stream()
                .filter(p -> p.id() == id)
                .findFirst();
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
}
