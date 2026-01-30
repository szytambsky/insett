package com.insett.indicesservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

@Document(indexName = "customers")
@Mapping(mappingPath = "mappings/index-mapping-customer.json")
public class Customer {
    @Id
    private String id;
    private String username;
    private String email;
    private Integer age;

    /**
     * Retrieves the customer's Elasticsearch document identifier.
     *
     * @return the customer's Elasticsearch document id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the Elasticsearch document identifier for this customer.
     *
     * @param id the document id to assign to this customer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the customer's username.
     *
     * @return the username of the customer, or {@code null} if not set.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the customer's username.
     *
     * @param username the username to assign to the customer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the customer's email address.
     *
     * @return the customer's email address, or {@code null} if not set
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer's email address.
     *
     * @param email the email address to assign to the customer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the customer's age.
     *
     * @return the customer's age in years, or {@code null} if not set
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the customer's age in years.
     *
     * @param age the age in years, or {@code null} to clear the value
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Create a string representation of the Customer including id, username, email, and age.
     *
     * @return a string containing the class name and field values in the format
     *         Customer{id='...', username='...', email='...', age=...}
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}