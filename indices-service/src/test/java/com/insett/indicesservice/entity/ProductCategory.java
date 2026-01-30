package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "categories")
public class ProductCategory {
    @Id
    private String id;

    @Field(name = "category", type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Integer)
    private Integer ordinalType;

    /**
     * Gets the Elasticsearch document ID for this product category.
     *
     * @return the document ID, or null if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Set the Elasticsearch document identifier for this product category.
     *
     * @param id the unique document id to assign to this category (maps to the Elasticsearch _id)
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the category name.
     *
     * @return the category name, or `null` if not set
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Set the product category name.
     *
     * This value is stored in the Elasticsearch document field named "category".
     *
     * @param categoryName the category name to store
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets the ordinal value representing this product category's type.
     *
     * @return the category type ordinal, or {@code null} if not set
     */
    public Integer getCategoryType() {
        return ordinalType;
    }

    /**
     * Set the category type using its ordinal value.
     *
     * @param ordinalType the ordinal value representing the category type, or {@code null} to clear it
     */
    public void setCategoryType(Integer ordinalType) {
        this.ordinalType = ordinalType;
    }
}