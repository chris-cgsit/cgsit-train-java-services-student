package com.cgsit.training.customer.data;

import com.cgsit.training.customer.model.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class CustomerStore {

    private final ConcurrentHashMap<Long, Customer> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @PostConstruct
    void init() {
        save(new Customer(null, "Anna Meier", "anna@example.com", "+43 664 1234567", "CGS IT Solutions", null));
        save(new Customer(null, "Bob Schmidt", "bob@example.com", "+49 170 9876543", "TechCorp", null));
        save(new Customer(null, "Clara Wagner", "clara@example.com", "+43 660 5551234", null, null));
        save(new Customer(null, "David Huber", "david@example.com", "+49 171 4445566", "FinanzAG", null));
        save(new Customer(null, "Eva Berger", "eva@example.com", "+43 699 7778899", "CGS IT Solutions", null));
    }

    public List<Customer> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(c -> c.email().equalsIgnoreCase(email));
    }

    public Customer save(Customer customer) {
        Long id = sequence.incrementAndGet();
        Customer created = new Customer(id, customer.name(), customer.email(),
                customer.phone(), customer.company(), LocalDateTime.now());
        store.put(id, created);
        return created;
    }

    public Optional<Customer> update(Long id, Customer customer) {
        if (!store.containsKey(id)) {
            return Optional.empty();
        }
        Customer existing = store.get(id);
        Customer updated = new Customer(id, customer.name(), customer.email(),
                customer.phone(), customer.company(), existing.createdAt());
        store.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
