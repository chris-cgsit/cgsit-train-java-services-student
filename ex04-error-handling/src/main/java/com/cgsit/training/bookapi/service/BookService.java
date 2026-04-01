package com.cgsit.training.bookapi.service;

import com.cgsit.training.bookapi.data.BookStore;
// TODO 1: Importiere BookNotFoundException und DuplicateBookException
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

    // TODO 2: Ändere Rückgabetyp zu Book und wirf BookNotFoundException statt Optional
    public Optional<Book> findById(long id) {
        return store.findById(id);
    }

    // TODO 3: Prüfe ob ISBN schon existiert und wirf DuplicateBookException
    public Book create(Book book) {
        return store.save(book);
    }

    // TODO 4: Wirf BookNotFoundException wenn Buch nicht gefunden
    public Optional<Book> update(long id, Book book) {
        return store.update(id, book);
    }

    // TODO 5: Wirf BookNotFoundException wenn Buch nicht gefunden
    public boolean delete(long id) {
        return store.delete(id);
    }
}
