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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return ordinalType;
    }

    public void setCategoryType(Integer ordinalType) {
        this.ordinalType = ordinalType;
    }
}
