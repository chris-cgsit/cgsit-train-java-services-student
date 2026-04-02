package com.cgsit.training.bookapi.unit;

import com.cgsit.training.bookapi.data.BookStore;
import com.cgsit.training.bookapi.exception.BookNotFoundException;
import com.cgsit.training.bookapi.exception.DuplicateBookException;
import com.cgsit.training.bookapi.model.Book;
import com.cgsit.training.bookapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService")
class BookServiceTest {

    @Mock
    BookStore store;

    @InjectMocks
    BookService service;

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("gibt Buch zurueck wenn gefunden")
        void shouldReturnBookWhenFound() {
            Book book = new Book(1L, "Clean Code", "Robert C. Martin", "978-0132350884", 34.99);
            when(store.findById(1L)).thenReturn(Optional.of(book));

            Book result = service.findById(1L);

            assertAll(
                    () -> assertEquals(1L, result.id()),
                    () -> assertEquals("Clean Code", result.title()),
                    () -> assertEquals("Robert C. Martin", result.author()),
                    () -> assertEquals(34.99, result.price())
            );
            verify(store).findById(1L);
        }

        @Test
        @DisplayName("wirft BookNotFoundException wenn nicht gefunden")
        void shouldThrowWhenNotFound() {
            when(store.findById(99L)).thenReturn(Optional.empty());

            assertThrows(BookNotFoundException.class,
                    () -> service.findById(99L));

            verify(store).findById(99L);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("erstellt Buch mit gueltigem Input")
        void shouldCreateBook() {
            Book input = new Book(0, "New Book", "Author", "978-1234567890", 29.99);
            Book saved = new Book(4L, "New Book", "Author", "978-1234567890", 29.99);
            when(store.existsByIsbn("978-1234567890")).thenReturn(false);
            when(store.save(input)).thenReturn(saved);

            Book result = service.create(input);

            assertAll(
                    () -> assertEquals(4L, result.id()),
                    () -> assertEquals("New Book", result.title()),
                    () -> assertEquals(29.99, result.price())
            );
            verify(store).existsByIsbn("978-1234567890");
            verify(store).save(input);
        }

        @Test
        @DisplayName("wirft DuplicateBookException bei doppelter ISBN")
        void shouldThrowOnDuplicateIsbn() {
            Book input = new Book(0, "Duplicate", "Author", "978-0132350884", 19.99);
            when(store.existsByIsbn("978-0132350884")).thenReturn(true);

            assertThrows(DuplicateBookException.class,
                    () -> service.create(input));

            verify(store).existsByIsbn("978-0132350884");
            verify(store, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("wirft BookNotFoundException wenn Buch nicht existiert")
        void shouldThrowWhenDeletingNonExistent() {
            when(store.delete(99L)).thenReturn(false);

            assertThrows(BookNotFoundException.class,
                    () -> service.delete(99L));

            verify(store).delete(99L);
        }
    }
}
