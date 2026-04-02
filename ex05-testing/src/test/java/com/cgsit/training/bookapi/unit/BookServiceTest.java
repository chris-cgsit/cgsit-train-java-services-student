package com.cgsit.training.bookapi.unit;

import com.cgsit.training.bookapi.data.BookStore;
import com.cgsit.training.bookapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: Importiere die noetigen Klassen:
// import com.cgsit.training.bookapi.model.Book;
// import com.cgsit.training.bookapi.exception.BookNotFoundException;
// import com.cgsit.training.bookapi.exception.DuplicateBookException;
// import java.util.Optional;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService")
class BookServiceTest {

    @Mock
    BookStore store;

    @InjectMocks
    BookService service;

    // TODO 1: Test "findById - Buch gefunden"
    // Hinweis: when(store.findById(1L)).thenReturn(Optional.of(book));
    //          Book result = service.findById(1L);
    //          assertEquals("Clean Code", result.title());

    // TODO 2: Test "findById - nicht gefunden, wirft BookNotFoundException"
    // Hinweis: when(store.findById(99L)).thenReturn(Optional.empty());
    //          assertThrows(BookNotFoundException.class, () -> service.findById(99L));

    // TODO 3: Test "create - gueltiges Buch"
    // Hinweis: when(store.existsByIsbn(isbn)).thenReturn(false);
    //          when(store.save(book)).thenReturn(savedBook);

    // TODO 4: Test "create - Duplikat (gleiche ISBN) wirft DuplicateBookException"
    // Hinweis: when(store.existsByIsbn(isbn)).thenReturn(true);

    // TODO 5: Test "delete - nicht gefunden wirft BookNotFoundException"
    // Hinweis: when(store.delete(99L)).thenReturn(false);
}
