package com.insett.indicesservice.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.insett.indicesservice.AbstractIndicesServiceTests;
import com.insett.indicesservice.domain.dao.ListingRepository;
import com.insett.indicesservice.entity.Listing;
import com.insett.indicesservice.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class QueryAnnotationTest extends AbstractIndicesServiceTests {

    private static final Logger log = LoggerFactory.getLogger(QueryAnnotationTest.class);

    @Autowired
    private ListingRepository repository;

    @Autowired
    ElasticsearchOperations operations;

    @BeforeAll
    public void dataSetup() {
        deleteRemnantsAndRefreshEsDb();
        List<Listing> listings
                = super.readResource("data/listings.json", new TypeReference<List<Listing>>() {});
        repository.saveAll(listings);
        Assertions.assertEquals(10, this.repository.count());
    }

    private void deleteRemnantsAndRefreshEsDb() {
        repository.deleteAll();
        operations.indexOps(Listing.class).refresh();
    }

    @Test
    public void searchListings() {
        SearchHits<Listing> searchHits = repository.search("electronic");
        searchHits.forEach(super.print());
        Assertions.assertEquals(1, searchHits.getTotalHits());
    }

}
