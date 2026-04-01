package com.cgsit.training.errorhandling.service;

import com.cgsit.training.errorhandling.data.ProductStore;
import com.cgsit.training.errorhandling.exception.DuplicateProductException;
import com.cgsit.training.errorhandling.exception.ProductNotFoundException;
import com.cgsit.training.errorhandling.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductStore store;

    public List<Product> findAll() {
        return store.findAll();
    }

    public Product findById(long id) {
        return store.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create(Product product) {
        if (store.existsByName(product.name())) {
            throw new DuplicateProductException(product.name());
        }
        return store.save(product);
    }

    public Product update(long id, Product product) {
        return store.update(id, product)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void delete(long id) {
        if (!store.delete(id)) {
            throw new ProductNotFoundException(id);
        }
    }
}
