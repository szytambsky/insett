package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "reviews")
@Setting(shards = 2, replicas = 2)
public class Reviews {
    @Id
    private String id;

    /**
     * Gets the Elasticsearch document identifier for this review.
     *
     * @return the document identifier string, or null if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the Elasticsearch document identifier for this Reviews entity.
     *
     * @param id the Elasticsearch document id to assign
     */
    public void setId(String id) {
        this.id = id;
    }
}