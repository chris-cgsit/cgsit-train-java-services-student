package com.cgsit.training.bookapi.data;

import com.cgsit.training.bookapi.model.Book;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@ApplicationScoped
public class BookStore {

    private static final Logger LOG = Logger.getLogger(BookStore.class.getName());

    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    // @PostConstruct läuft einmalig beim Deployment (wenn WildFly die Bean erzeugt).
    // Änderungen hier brauchen ein Re-Deploy (wildfly:dev erkennt das automatisch).
    // Änderungen in normalen Methoden (findAll, save, ...) werden sofort per Hot-Swap aktiv.
    @PostConstruct
    void init() {
        LOG.info(">>> BookStore @PostConstruct — Testdaten werden geladen...");
        // TODO: Füge 3-5 Beispiel-Bücher hinzu mit save()
        // Beispiel: save(new Book(0, "Clean Code", "Robert C. Martin", "978-0132350884", 34.99));
        LOG.info(">>> BookStore bereit — " + books.size() + " Bücher geladen.");
    }

    public List<Book> findAll() {
        // TODO: Alle Bücher als unveränderliche Kopie zurückgeben
        return List.of();
    }

    public Optional<Book> findById(long id) {
        // TODO: Buch mit der gegebenen ID suchen und zurückgeben
        return Optional.empty();
    }

    // Wir erzeugen hier ein neues Book mit generierter ID, weil Records immutable sind.
    // In einer echten Anwendung übernimmt das die Datenbank (JPA/Hibernate):
    //   @Id @GeneratedValue private Long id;  → DB vergibt die ID automatisch.
    // Unser In-Memory Store simuliert das mit AtomicLong.
    public Book save(Book book) {
        // TODO: Neues Buch mit auto-generierter ID erstellen und zur Liste hinzufügen
        // Hinweis: idCounter.incrementAndGet() für die nächste ID
        // Hinweis: new Book(id, book.title(), book.author(), ...) — Record ist immutable
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
