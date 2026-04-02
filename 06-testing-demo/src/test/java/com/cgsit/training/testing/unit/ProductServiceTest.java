package com.cgsit.training.testing.unit;

import com.cgsit.training.testing.exception.ProductNotFoundException;
import com.cgsit.training.testing.model.Product;
import com.cgsit.training.testing.repository.ProductRepository;
import com.cgsit.training.testing.service.ProductService;
import com.cgsit.training.testing.util.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService")
class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductService service;

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("gibt Produkt zurueck wenn gefunden")
        void shouldReturnProductWhenFound() {
            Product laptop = TestData.laptop();
            when(repository.findById(1L)).thenReturn(Optional.of(laptop));

            Product result = service.findById(1L);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals("Laptop", result.name()),
                    () -> assertEquals(999.99, result.price())
            );
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("wirft Exception wenn nicht gefunden")
        void shouldThrowWhenNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class,
                    () -> service.findById(99L));

            verify(repository).findById(99L);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("erstellt Produkt mit gueltigem Input")
        void shouldCreateProduct() {
            Product input = TestData.newProduct("Tastatur", 79.99);
            Product saved = new Product(4L, "Tastatur", 79.99);
            when(repository.save(input)).thenReturn(saved);

            Product result = service.create(input);

            assertAll(
                    () -> assertNotNull(result.id()),
                    () -> assertEquals("Tastatur", result.name()),
                    () -> assertEquals(79.99, result.price())
            );
            verify(repository).save(input);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("gibt alle Produkte zurueck")
        void shouldReturnAllProducts() {
            when(repository.findAll()).thenReturn(
                    List.of(TestData.laptop(), TestData.maus()));

            List<Product> result = service.findAll();

            assertEquals(2, result.size());
            verify(repository).findAll();
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("loescht vorhandenes Produkt")
        void shouldDeleteExistingProduct() {
            when(repository.findById(1L)).thenReturn(Optional.of(TestData.laptop()));

            assertDoesNotThrow(() -> service.delete(1L));

            verify(repository).delete(1L);
        }

        @Test
        @DisplayName("wirft Exception beim Loeschen eines nicht vorhandenen Produkts")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class,
                    () -> service.delete(99L));

            verify(repository, never()).delete(any());
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("findById mit verschiedenen IDs")
    void shouldFindByVariousIds(long id) {
        Product product = new Product(id, "Product-" + id, 10.0 * id);
        when(repository.findById(id)).thenReturn(Optional.of(product));

        Product result = service.findById(id);

        assertEquals(id, result.id());
        verify(repository).findById(id);
    }
}
