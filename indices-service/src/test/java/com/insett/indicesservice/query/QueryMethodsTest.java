package com.insett.indicesservice.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.insett.indicesservice.AbstractIndicesServiceTests;
import com.insett.indicesservice.domain.dao.ProductRepository;
import com.insett.indicesservice.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;

import java.util.List;

public class QueryMethodsTest extends AbstractIndicesServiceTests {

    private static final Logger log = LoggerFactory.getLogger(QueryMethodsTest.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    ElasticsearchOperations operations;

    @BeforeAll
    public void dataSetup() {
        deleteRemnantsAndRefreshEsDb();
        List<Product> products = super.readResource("data/products.json", new TypeReference<List<Product>>() {
        });
        this.repository.saveAll(products);
        Assertions.assertEquals(20, this.repository.count());
    }

    private void deleteRemnantsAndRefreshEsDb() {
        repository.deleteAll();
        operations.indexOps(Product.class).refresh();
    }

    @Test
    public void findByCategory() {
        SearchHits<Product> searchHits = this.repository.findByCategory("Furniture");
        searchHits.forEach(super.print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    @Test
    public void findByCategories() {
        SearchHits<Product> searchHits = this.repository.findByCategoryIn(List.of("Furniture", "Electronics"));
        searchHits.forEach(super.print());
        Assertions.assertEquals(16, searchHits.getTotalHits());
    }

    @Test
    public void findByTitleAndCategory() {
        SearchHits<Product> searchHits = this.repository.findByCategoryAndBrand("Furniture", "Ikea");
        searchHits.forEach(super.print());
        Assertions.assertEquals(2, searchHits.getTotalHits());
    }

    @Test
    public void findByPriceLessThan() {
        SearchHits<Product> searchHits = this.repository.findByPriceLessThan(50);
        searchHits.forEach(super.print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    @Test
    public void findByPriceBetween() {
        SearchHits<Product> searchHits = this.repository.findByPriceBetween(
                49, 129, Sort.by(Sort.Direction.DESC, "price"));
        searchHits.forEach(super.print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    @Test
    public void findByCategoryWithPagination() {
        SearchPage<Product> searchPage = this.repository.findByCategory(
                "Furniture", PageRequest.of(0, 2));
        searchPage.forEach(super.print());
        Assertions.assertEquals(4, searchPage.getSearchHits().getTotalHits());
        Assertions.assertEquals(2, searchPage.getPageable().getPageSize());
    }
}
