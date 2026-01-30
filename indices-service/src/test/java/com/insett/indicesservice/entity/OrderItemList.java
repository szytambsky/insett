package com.insett.indicesservice.entity;

import java.math.BigDecimal;

public class OrderItemList {
    private String orderItemId;
    private String orderId;
    private String productId;
    private String listingId;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private String sku;
    private String orderItemStatus; //(PENDING, RESERVED, FULFILLED, CANCELLED)
}
