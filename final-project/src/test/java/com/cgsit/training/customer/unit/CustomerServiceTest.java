package com.cgsit.training.customer.unit;

import com.cgsit.training.customer.data.CustomerStore;
import com.cgsit.training.customer.exception.CustomerNotFoundException;
import com.cgsit.training.customer.exception.DuplicateCustomerException;
import com.cgsit.training.customer.model.Customer;
import com.cgsit.training.customer.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    CustomerStore store;

    @InjectMocks
    CustomerService service;

    static Customer testCustomer(Long id, String name, String email) {
        return new Customer(id, name, email, "TestCorp", LocalDateTime.now());
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("should return all customers")
        void shouldReturnAll() {
            when(store.findAll()).thenReturn(List.of(
                    testCustomer(1L, "Anna", "anna@test.com"),
                    testCustomer(2L, "Bob", "bob@test.com")));

            List<Customer> result = service.findAll();

            assertEquals(2, result.size());
            verify(store).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return customer when found")
        void shouldReturnCustomer() {
            Customer anna = testCustomer(1L, "Anna", "anna@test.com");
            when(store.findById(1L)).thenReturn(Optional.of(anna));

            Customer result = service.findById(1L);

            assertEquals("Anna", result.name());
            assertEquals("anna@test.com", result.email());
            verify(store).findById(1L);
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when not found")
        void shouldThrowWhenNotFound() {
            when(store.findById(999L)).thenReturn(Optional.empty());

            var ex = assertThrows(CustomerNotFoundException.class,
                    () -> service.findById(999L));

            assertEquals(404, ex.getStatusCode());
            assertTrue(ex.getMessage().contains("999"));
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should create customer when email is unique")
        void shouldCreateCustomer() {
            Customer input = testCustomer(null, "Clara", "clara@test.com");
            Customer saved = testCustomer(3L, "Clara", "clara@test.com");

            when(store.existsByEmail("clara@test.com")).thenReturn(false);
            when(store.save(input)).thenReturn(saved);

            Customer result = service.create(input);

            assertEquals(3L, result.id());
            assertEquals("Clara", result.name());
            verify(store).existsByEmail("clara@test.com");
            verify(store).save(input);
        }

        @Test
        @DisplayName("should throw DuplicateCustomerException when email exists")
        void shouldThrowOnDuplicate() {
            Customer input = testCustomer(null, "Anna2", "anna@test.com");
            when(store.existsByEmail("anna@test.com")).thenReturn(true);

            var ex = assertThrows(DuplicateCustomerException.class,
                    () -> service.create(input));

            assertEquals(409, ex.getStatusCode());
            assertTrue(ex.getMessage().contains("anna@test.com"));
            verify(store, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("should update customer when found")
        void shouldUpdateCustomer() {
            Customer input = testCustomer(null, "Anna Updated", "anna@test.com");
            Customer updated = testCustomer(1L, "Anna Updated", "anna@test.com");

            when(store.update(1L, input)).thenReturn(Optional.of(updated));

            Customer result = service.update(1L, input);

            assertEquals("Anna Updated", result.name());
            verify(store).update(1L, input);
        }

        @Test
        @DisplayName("should throw when customer not found")
        void shouldThrowWhenNotFound() {
            Customer input = testCustomer(null, "Ghost", "ghost@test.com");
            when(store.update(999L, input)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class,
                    () -> service.update(999L, input));
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("should delete customer when found")
        void shouldDeleteCustomer() {
            when(store.delete(1L)).thenReturn(true);

            assertDoesNotThrow(() -> service.delete(1L));
            verify(store).delete(1L);
        }

        @Test
        @DisplayName("should throw when customer not found")
        void shouldThrowWhenNotFound() {
            when(store.delete(999L)).thenReturn(false);

            assertThrows(CustomerNotFoundException.class,
                    () -> service.delete(999L));
        }
    }
}
