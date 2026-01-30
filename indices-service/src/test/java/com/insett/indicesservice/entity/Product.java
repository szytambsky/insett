package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
*    our goal here is to verify the creation of index with the below setting.
*    that is why, entity class does not have any other properties
**/
@Document(indexName = "products")
@Mapping(mappingPath = "mappings/index-mapping-product.json")
public class Product {
    @Id
    private String id;
    private String title;
    private String description;
    private String category;
    private String brand;
    private Integer price;
    private Integer inStock;

    /**
     * Get the product's identifier.
     *
     * @return the product identifier, or null if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product's identifier.
     *
     * @param id the identifier to assign to this product
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the product title.
     *
     * @return the product title, or {@code null} if not set
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the product title.
     *
     * @param title the title to assign to the product
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the product description.
     *
     * @return the product description, or {@code null} if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description.
     *
     * @param description the new description for the product
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the product's category.
     *
     * @return the category of the product, or `null` if not set.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the product category.
     *
     * @param category the category name for the product
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Retrieve the product's brand name.
     *
     * @return the product's brand name, or {@code null} if not set
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Set the product's brand name.
     *
     * @param brand the brand or manufacturer name to assign to the product
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Retrieves the product's price.
     *
     * @return the price of the product, or `null` if unspecified
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Sets the product's price.
     *
     * @param price the product price, or {@code null} to unset it
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * Gets the number of items available in stock.
     *
     * @return the available stock count, or {@code null} if not set
     */
    public Integer getInStock() {
        return inStock;
    }

    /**
     * Sets the number of items available in stock.
     *
     * @param inStock the quantity of items available; may be {@code null} to indicate unknown or unset
     */
    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    /**
     * String representation of the product including all fields.
     *
     * @return the string containing `id`, `title`, `description`, `category`, `brand`, `price`, and `inStock`
     */
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                '}';
    }
}