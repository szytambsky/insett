package com.insett.indicesservice;

import com.insett.indicesservice.entity.ProductCategory;
import com.insett.indicesservice.entity.Listing;
import com.insett.indicesservice.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public class IndexOperationsTest extends AbstractIndicesServiceTests {
    private static final Logger log = LoggerFactory.getLogger(IndexOperationsTest.class);
    private static final String PRODUCTS_INDEX = "products";

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    public void createIndex() {
        IndexOperations indexOperations = this.elasticsearchOperations.indexOps(IndexCoordinates.of(PRODUCTS_INDEX));
        Assertions.assertTrue(indexOperations.create());
        verify(indexOperations, 1, 1);
    }

    @Test
    public void createIndexWithSettings() {
        IndexOperations indexOperations = this.elasticsearchOperations.indexOps(Product.class);
        Assertions.assertTrue(indexOperations.create());
        verify(indexOperations, 2, 2);
    }

    @Test
    public void createIndexWithSettingsAndMappings() {
        IndexOperations indexOperations = this.elasticsearchOperations.indexOps(Listing.class);
        Assertions.assertTrue(indexOperations.createWithMapping());
        verify(indexOperations, 3, 0);
    }

    @Test
    public void createIndexWithFieldMappings() {
        IndexOperations indexOperations = this.elasticsearchOperations.indexOps(ProductCategory.class);
        Assertions.assertTrue(indexOperations.createWithMapping());
        verify(indexOperations, 1, 1);
    }

    private void verify(IndexOperations indexOperations, int expectedNumberOfShards, int expectedNumberOfReplicas) {
        Settings settings = indexOperations.getSettings();
        log.info("Settings: {}", settings);
        log.info("mapping: {}", indexOperations.getMapping());
        Assertions.assertEquals(String.valueOf(expectedNumberOfShards), settings.get("index.number_of_shards"));
        Assertions.assertEquals(String.valueOf(expectedNumberOfReplicas), settings.get("index.number_of_replicas"));
        deleteIndex(indexOperations);
    }

    private void deleteIndex(IndexOperations indexOperations) {
        Assertions.assertTrue(indexOperations.delete());
    }
}
