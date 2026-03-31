package com.cgsit.training.rest.repository;

import com.cgsit.training.rest.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@ApplicationScoped
public class ProductRepository {

    private static final Logger LOG = Logger.getLogger(ProductRepository.class.getName());

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    // @PostConstruct läuft einmalig beim Deployment (wenn WildFly die Bean erzeugt).
    // Änderungen hier brauchen ein Re-Deploy (wildfly:dev erkennt das automatisch).
    // Änderungen in normalen Methoden (findAll, save, ...) werden sofort per Hot-Swap aktiv.
    @PostConstruct
    void init() {
        LOG.info(">>> ProductRepository @PostConstruct — Testdaten werden geladen...");
        save(new Product(0, "Laptop", 999.99, "Electronics"));
        save(new Product(0, "Tastatur", 79.99, "Peripherie"));
        save(new Product(0, "Monitor 27\"", 349.00, "Electronics"));
        save(new Product(0, "USB-C Hub", 45.50, "Peripherie"));
        save(new Product(0, "Webcam HD", 89.00, "Peripherie"));
        LOG.info(">>> ProductRepository bereit — " + products.size() + " Produkte geladen.");
    }

    public List<Product> findAll() {
        return List.copyOf(products);
    }

    public Optional<Product> findById(long id) {
        return products.stream()
                .filter(p -> p.id() == id)
                .findFirst();
    }

    // Wir erzeugen hier ein neues Product mit generierter ID, weil Records immutable sind.
    // In einer echten Anwendung übernimmt das die Datenbank (JPA/Hibernate):
    //   @Id @GeneratedValue private Long id;  → DB vergibt die ID automatisch.
    // Unser In-Memory Store simuliert das mit AtomicLong.
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
