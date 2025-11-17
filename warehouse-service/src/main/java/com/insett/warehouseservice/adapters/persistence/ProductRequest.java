package com.insett.warehouseservice.adapters.persistence;

import java.math.BigDecimal;

public record ProductRequest(
        String productName,
        String description,
        BigDecimal price,
        String sku,
        String imageUrl) {
}
