package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    Optional<Product> findByTitleAndBrand(String title, String brand);

    List<Product> findAllByTitleAndCategory(String title, String category);

    SearchHits<Product> findByTitle(String name);

    SearchHits<Product> findByCategoryAndBrand(String category, String brand);

    SearchHits<Product> findByCategory(String category);

    SearchPage<Product> findByCategory(String category, Pageable pageable);

    SearchHits<Product> findByCategoryIn(List<String> categories);

    SearchHits<Product> findByPriceLessThan(Integer price);

    SearchHits<Product> findByPriceBetween(Integer from, Integer to, Sort sort);
}
