package com.cgsit.training.bookapi.service;

import com.cgsit.training.bookapi.data.BookStore;
import com.cgsit.training.bookapi.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class BookService {

    @Inject
    BookStore store;

    // Validator is provided by CDI — Hibernate Validator on WildFly.
    // Use this for programmatic validation when @Valid on the resource
    // is not sufficient (e.g. conditional logic, batch processing).
    @Inject
    Validator validator;

    public List<Book> findAll() {
        return store.findAll();
    }

    public Optional<Book> findById(long id) {
        return store.findById(id);
    }

    public Book create(Book book) {
        return store.save(book);
    }

    public Optional<Book> update(long id, Book book) {
        return store.update(id, book);
    }

    public boolean delete(long id) {
        return store.delete(id);
    }

    /**
     * Programmatic validation — validates a Book manually in the service layer.
     *
     * Use case: when validation cannot happen at the JAX-RS level, e.g.:
     *   - Batch import of multiple books
     *   - Books created from internal events (not HTTP requests)
     *   - Conditional validation based on business rules
     *
     * Returns the validated book, or throws ConstraintViolationException.
     */
    public Book validateAndCreate(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    "Book validation failed", violations);
        }
        return store.save(book);
    }

    /**
     * Batch import — validates each book programmatically.
     * Collects all valid books, skips invalid ones.
     * Returns only the successfully created books.
     */
    public List<Book> importBooks(List<Book> books) {
        return books.stream()
                .filter(book -> validator.validate(book).isEmpty())
                .map(store::save)
                .toList();
    }
}
