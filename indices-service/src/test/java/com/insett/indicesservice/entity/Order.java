package com.insett.indicesservice.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.math.BigDecimal;

@Document(indexName = "orders")
@Mapping(mappingPath = "mappings/index-mapping-order.json")
public class Order {
    @Id
    private String orderId;
    private String paymentStatus;
    private String orderStatus;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String billingAddress;

    /**
     * Gets the unique identifier for the order.
     *
     * @return the order's identifier
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the order's identifier used as the document ID in Elasticsearch.
     *
     * @param orderId the unique identifier for this order
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the payment status of the order.
     *
     * @return the payment status, or {@code null} if not set
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the payment status of the order.
     *
     * @param paymentStatus the payment status to assign to the order (for example: "PAID", "PENDING", "FAILED")
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Gets the order status.
     *
     * @return the order status, or {@code null} if it has not been set
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the order's status.
     *
     * @param orderStatus the new status of the order
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Retrieves the order's reference number.
     *
     * @return the order's reference number, or null if not set
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order's reference number used to identify the order in external systems.
     *
     * @param orderNumber the external order number or reference
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Retrieves the total amount for the order.
     *
     * @return the total amount of the order as a BigDecimal, or null if not set
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the monetary total for this order.
     *
     * @param totalAmount the total amount of the order as a BigDecimal
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the billing address of the order.
     *
     * @return the billing address of the order, or {@code null} if not set
     */
    public String getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing address for this order.
     *
     * @param billingAddress the billing address to assign to the order
     */
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Creates a string representation of this Order containing all field values in a structured format.
     *
     * @return a string in the form "Order{orderId=..., paymentStatus=..., orderStatus=..., orderNumber=..., totalAmount=..., billingAddress=...}"
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", totalAmount=" + totalAmount +
                ", billingAddress='" + billingAddress + '\'' +
                '}';
    }
}