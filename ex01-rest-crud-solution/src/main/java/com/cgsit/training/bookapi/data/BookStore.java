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
        save(new Book(0, "Clean Code", "Robert C. Martin", "978-0132350884", 34.99));
        save(new Book(0, "Effective Java", "Joshua Bloch", "978-0134685991", 42.50));
        save(new Book(0, "Head First Design Patterns", "Eric Freeman", "978-0596007126", 39.99));
        save(new Book(0, "Java Concurrency in Practice", "Brian Goetz", "978-0321349606", 45.00));
        save(new Book(0, "Refactoring", "Martin Fowler", "978-0134757599", 47.99));
        LOG.info(">>> BookStore bereit — " + books.size() + " Bücher geladen.");
    }

    public List<Book> findAll() {
        return List.copyOf(books);
    }

    public Optional<Book> findById(long id) {
        return books.stream()
                .filter(b -> b.id() == id)
                .findFirst();
    }

    // Wir erzeugen hier ein neues Book mit generierter ID, weil Records immutable sind.
    // In einer echten Anwendung übernimmt das die Datenbank (JPA/Hibernate):
    //   @Id @GeneratedValue private Long id;  → DB vergibt die ID automatisch.
    // Unser In-Memory Store simuliert das mit AtomicLong.
    public Book save(Book book) {
        Book created = new Book(
                idCounter.incrementAndGet(),
                book.title(),
                book.author(),
                book.isbn(),
                book.price()
        );
        books.add(created);
        return created;
    }

    public Optional<Book> update(long id, Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id() == id) {
                Book updated = new Book(id, book.title(), book.author(), book.isbn(), book.price());
                books.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public boolean delete(long id) {
        return books.removeIf(b -> b.id() == id);
    }
}
