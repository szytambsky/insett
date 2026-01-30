package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends ElasticsearchRepository<Customer, String> {

    Optional<Customer> findCustomerByUsernameAndEmail(String username, String email);
}
