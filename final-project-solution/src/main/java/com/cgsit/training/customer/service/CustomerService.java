package com.cgsit.training.customer.service;

import com.cgsit.training.customer.data.CustomerStore;
import com.cgsit.training.customer.exception.CustomerNotFoundException;
import com.cgsit.training.customer.exception.DuplicateCustomerException;
import com.cgsit.training.customer.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerStore store;

    public List<Customer> findAll() {
        return store.findAll();
    }

    public Customer findById(Long id) {
        return store.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer create(Customer customer) {
        if (store.existsByEmail(customer.email())) {
            throw new DuplicateCustomerException(customer.email());
        }
        return store.save(customer);
    }

    public Customer update(Long id, Customer customer) {
        return store.update(id, customer)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public void delete(Long id) {
        if (!store.delete(id)) {
            throw new CustomerNotFoundException(id);
        }
    }
}
