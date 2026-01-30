package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends ElasticsearchRepository<Customer, String> {

    /**
 * Finds a customer matching the given username and email.
 *
 * @param username the customer's username to match
 * @param email the customer's email to match
 * @return an Optional containing the matching Customer if present, otherwise an empty Optional
 */
Optional<Customer> findCustomerByUsernameAndEmail(String username, String email);
}