package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    Optional<Product> findProductByTitleAndBrand(String title, String brand);
}
