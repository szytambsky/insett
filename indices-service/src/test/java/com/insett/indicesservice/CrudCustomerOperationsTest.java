package com.insett.indicesservice;

import com.insett.indicesservice.domain.dao.CustomerRepository;
import com.insett.indicesservice.entity.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CrudCustomerOperationsTest extends AbstractIndicesServiceTests {
    private static final Logger log = LoggerFactory.getLogger(CrudCustomerOperationsTest.class);

    @Autowired
    private CustomerRepository repository;

    @Test
    public void crudOperations() {
        var givenName = "SamCards";
        var givenEmail = "sam.cards@gmail.com";
        var givenAge = 28;
        Customer customer = createCustomer(givenName, givenEmail, givenAge);

        repository.save(customer);
        printAll();
        customer = repository.findCustomerByUsernameAndEmail(givenName, givenEmail).orElseThrow();
        Assertions.assertEquals(givenName, customer.getUsername());
        Assertions.assertEquals(givenEmail, customer.getEmail());
        Assertions.assertEquals(givenAge, customer.getAge());

        customer.setAge(30);
        customer = repository.save(customer);
        printAll();
        Assertions.assertEquals(30, customer.getAge());

        repository.deleteById(customer.getId());
        Assertions.assertFalse(repository.existsById(customer.getId()));
    }

    private Customer createCustomer(String username, String email, Integer age) {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setAge(age);
        return customer;
    }

    private void printAll() {
        repository.findAll()
                .forEach(customer -> log.info("customer: {}", customer));
    }
}
