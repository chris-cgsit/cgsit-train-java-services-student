package com.cgsit.training.bookapi.service;

import com.cgsit.training.bookapi.data.BookStore;
import com.cgsit.training.bookapi.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookService {

    @Inject
    BookStore store;

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
}
