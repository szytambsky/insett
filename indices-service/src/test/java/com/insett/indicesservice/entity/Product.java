package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
*    our goal here is to verify the creation of index with the below setting.
*    that is why, entity class does not have any other properties
**/
@Document(indexName = "products")
@Setting(shards = 2, replicas = 2)
public class Product {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
