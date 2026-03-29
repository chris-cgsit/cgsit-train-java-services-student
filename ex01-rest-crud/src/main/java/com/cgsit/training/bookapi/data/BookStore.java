package com.cgsit.training.bookapi.data;

import com.cgsit.training.bookapi.model.Book;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class BookStore {

    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @PostConstruct
    void init() {
        // TODO: Füge 3-5 Beispiel-Bücher hinzu mit save()
        // Beispiel: save(new Book(0, "Clean Code", "Robert C. Martin", "978-0132350884", 34.99));
    }

    public List<Book> findAll() {
        // TODO: Alle Bücher als unveränderliche Kopie zurückgeben
        return List.of();
    }

    public Optional<Book> findById(long id) {
        // TODO: Buch mit der gegebenen ID suchen und zurückgeben
        return Optional.empty();
    }

    public Book save(Book book) {
        // TODO: Neues Buch mit auto-generierter ID erstellen und zur Liste hinzufügen
        // Hinweis: idCounter.incrementAndGet() für die nächste ID
        return book;
    }

    public Optional<Book> update(long id, Book book) {
        // TODO: Buch mit der ID finden und mit neuen Daten ersetzen
        // Rückgabe: Optional.of(updated) wenn gefunden, Optional.empty() wenn nicht
        return Optional.empty();
    }

    public boolean delete(long id) {
        // TODO: Buch mit der ID löschen
        // Rückgabe: true wenn gelöscht, false wenn nicht gefunden
        return false;
    }
}
