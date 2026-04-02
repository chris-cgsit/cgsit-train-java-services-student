package com.cgsit.training.testing.repository;

import com.cgsit.training.testing.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @PostConstruct
    void init() {
        save(new Product(null, "Mechanical Keyboard", 89.99));
        save(new Product(null, "USB-C Hub", 34.50));
        save(new Product(null, "IntelliJ IDEA License", 499.00));
        save(new Product(null, "Clean Code", 34.99));
        save(new Product(null, "27\" Monitor", 349.00));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Product save(Product product) {
        if (product.id() == null) {
            long id = sequence.getAndIncrement();
            Product saved = product.withId(id);
            store.put(id, saved);
            return saved;
        }
        store.put(product.id(), product);
        return product;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}
