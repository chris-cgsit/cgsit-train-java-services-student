package com.cgsit.training.servicelayer.service;

import com.cgsit.training.servicelayer.model.Product;
import com.cgsit.training.servicelayer.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service layer — contains BUSINESS LOGIC only.
 *
 * This layer:
 * - Knows nothing about HTTP (no Request, Response, Status codes)
 * - Works with Java objects (Product, List, Optional)
 * - Applies business rules (filtering, sorting, validation logic)
 * - Delegates data access to the Repository
 *
 * Can be reused from REST, CLI, Batch, Tests — without any HTTP dependency.
 */
@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    /**
     * Returns all valid products (price > 0), sorted by the given field.
     * Business rule: products with negative price are filtered out.
     */
    public List<Product> findAll(String sortBy) {
        Comparator<Product> comparator = "price".equalsIgnoreCase(sortBy)
                ? Comparator.comparingDouble(Product::price)
                : Comparator.comparing(Product::name);

        return repository.findAll().stream()
                .filter(p -> p.price() > 0)     // Business rule: no negative prices
                .sorted(comparator)
                .toList();
    }

    /**
     * Returns a product by ID, or empty if not found.
     */
    public Optional<Product> findById(long id) {
        return repository.findById(id);
    }

    /**
     * Creates a new product after applying business validation.
     *
     * @throws IllegalArgumentException if name is blank or price is not positive
     */
    public Product create(Product product) {
        if (product.name() == null || product.name().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.price() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        return repository.save(product);
    }
}
