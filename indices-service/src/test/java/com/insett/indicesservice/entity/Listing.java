package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "listings")
@Setting(settingPath = "settings/index-setting-listing.json")
@Mapping(mappingPath = "mappings/index-mapping-listing.json")
public class Listing {
    @Id
    private String id;
    private String name;
    private String category;
    private String brand;
    private Integer price;
    private Integer inStock;

    /**
     * Gets the listing identifier.
     *
     * @return the listing identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the Elasticsearch document identifier for this listing.
     *
     * @param id the identifier to assign to the listing document
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the listing's name.
     *
     * @return the listing's name, or {@code null} if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the listing's name.
     *
     * @param name the name to assign to this listing
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the listing's category.
     *
     * @return the category of the listing, or null if not set
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the listing's category.
     *
     * @param category the category name to assign to this listing
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the brand of the listing.
     *
     * @return the brand of the listing, or `null` if not set
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the listing.
     *
     * @param brand the brand name to assign to this listing
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the listing's price.
     *
     * @return the price of the listing, or {@code null} if not set
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Sets the listing's price.
     *
     * @param price the price to assign to this listing
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * Retrieves the quantity of items available in stock.
     *
     * @return the number of items currently in stock, or `null` if not set
     */
    public Integer getInStock() {
        return inStock;
    }

    /**
     * Sets the number of items available in stock.
     *
     * @param inStock the quantity of items available, or {@code null} if unspecified
     */
    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }
}