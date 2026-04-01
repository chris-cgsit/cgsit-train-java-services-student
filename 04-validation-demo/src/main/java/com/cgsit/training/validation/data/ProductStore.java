package com.cgsit.training.validation.data;

import com.cgsit.training.validation.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
        save(new Product(0, "Mechanical Keyboard", new BigDecimal("89.99"), "Peripherie",
                LocalDate.now(), LocalTime.of(9, 0), new BigDecimal("0.85")));
        save(new Product(0, "USB-C Hub", new BigDecimal("34.50"), "Electronics",
                LocalDate.now(), LocalTime.of(8, 0), new BigDecimal("0.12")));
        save(new Product(0, "IntelliJ IDEA License", new BigDecimal("499.00"), "Software",
                LocalDate.now(), LocalTime.of(0, 0), null));
        save(new Product(0, "Clean Code", new BigDecimal("34.99"), "Books",
                LocalDate.now(), LocalTime.of(9, 0), new BigDecimal("0.65")));
        save(new Product(0, "27\" Monitor", new BigDecimal("349.00"), "Electronics",
                LocalDate.now().plusDays(7), LocalTime.of(10, 0), new BigDecimal("5.40")));
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
                product.category(),
                product.availableFrom(),
                product.availableUntil(),
                product.weight()
        );
        products.add(created);
        return created;
    }

    public Optional<Product> update(long id, Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).id() == id) {
                Product updated = new Product(id, product.name(), product.price(),
                        product.category(), product.availableFrom(),
                        product.availableUntil(), product.weight());
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
