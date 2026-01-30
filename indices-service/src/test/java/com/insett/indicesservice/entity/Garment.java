package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.util.List;

@Document(indexName = "garments")
@Mapping(mappingPath = "mappings/index-mapping-garment.json")
public class Garment {

    @Id
    private String id;
    private String name;
    private Integer price;
    private List<String> color;
    private List<String> size;
    private String material;
    private String brand;
    private String occasion;
    private String neckStyle;

    /**
     * Retrieve the document identifier for this garment.
     *
     * @return the identifier of this garment, or {@code null} if not set
     */
    public String getId() {
        return id;
    }

    /**
     * Set the document identifier for this garment.
     *
     * @param id the identifier for this Garment document
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the garment's name.
     *
     * @return the garment name, or {@code null} if none is set
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the garment's name.
     *
     * @param name the name to assign to this garment
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the garment's price.
     *
     * @return the price of the garment, or {@code null} if not set
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Sets the garment's price.
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * Gets the garment's available colors.
     *
     * @return the list of available colors for the garment, or null if not set
     */
    public List<String> getColor() {
        return color;
    }

    /**
     * Sets the list of available colors for the garment.
     *
     * @param color list of color names; may be {@code null} to clear the colors
     */
    public void setColor(List<String> color) {
        this.color = color;
    }

    /**
     * Returns the list of available sizes for this garment.
     *
     * @return the list of sizes, or {@code null} if not set
     */
    public List<String> getSize() {
        return size;
    }

    /**
     * Sets the list of available sizes for this garment.
     *
     * @param size the list of sizes (for example "S", "M", "L")
     */
    public void setSize(List<String> size) {
        this.size = size;
    }

    /**
     * Gets the material of the garment.
     *
     * @return the material of the garment, or null if not set
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Sets the material of the garment.
     *
     * @param material the material or fabric of the garment (for example: "cotton", "polyester")
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * Gets the garment's brand.
     *
     * @return the brand of the garment, or {@code null} if not set
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the garment's brand.
     *
     * @param brand the brand name to assign to the garment
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the occasion for which the garment is intended.
     *
     * @return the garment's occasion, or {@code null} if not set
     */
    public String getOccasion() {
        return occasion;
    }

    /**
     * Set the occasion associated with the garment.
     *
     * @param occasion a label describing the intended occasion for the garment (for example, "casual" or "formal")
     */
    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    /**
     * Gets the garment's neck style.
     *
     * @return the neck style of the garment, or {@code null} if not set
     */
    public String getNeckStyle() {
        return neckStyle;
    }

    /**
     * Sets the garment's neck style.
     *
     * @param neckStyle the neck style to assign to the garment
     */
    public void setNeckStyle(String neckStyle) {
        this.neckStyle = neckStyle;
    }

    /**
     * Provide a string representation of this Garment including all fields.
     *
     * @return the `String` containing this Garment's id, name, price, color, size, material, brand, occasion, and neckStyle
     */
    @Override
    public String toString() {
        return "Garment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", color=" + color +
                ", size=" + size +
                ", material='" + material + '\'' +
                ", brand='" + brand + '\'' +
                ", occasion='" + occasion + '\'' +
                ", neckStyle='" + neckStyle + '\'' +
                '}';
    }
}