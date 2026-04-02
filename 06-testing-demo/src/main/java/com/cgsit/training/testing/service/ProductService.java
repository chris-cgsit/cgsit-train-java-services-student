package com.cgsit.training.testing.service;

import com.cgsit.training.testing.exception.ProductNotFoundException;
import com.cgsit.training.testing.model.Product;
import com.cgsit.training.testing.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product update(Long id, Product product) {
        // Verify the product exists
        findById(id);
        return repository.save(product.withId(id));
    }

    public void delete(Long id) {
        // Verify the product exists
        findById(id);
        repository.delete(id);
    }
}
