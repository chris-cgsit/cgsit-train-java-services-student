package com.cgsit.training.bookapi.service;

import com.cgsit.training.bookapi.data.BookStore;
import com.cgsit.training.bookapi.exception.BookNotFoundException;
import com.cgsit.training.bookapi.exception.DuplicateBookException;
import com.cgsit.training.bookapi.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    BookStore store;

    public List<Book> findAll() {
        return store.findAll();
    }

    public Book findById(long id) {
        return store.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book create(Book book) {
        if (store.existsByIsbn(book.isbn())) {
            throw new DuplicateBookException(book.isbn());
        }
        return store.save(book);
    }

    public Book update(long id, Book book) {
        return store.update(id, book)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public void delete(long id) {
        if (!store.delete(id)) {
            throw new BookNotFoundException(id);
        }
    }
}
