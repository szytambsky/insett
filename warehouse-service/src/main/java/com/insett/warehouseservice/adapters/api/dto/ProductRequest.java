package com.insett.warehouseservice.adapters.api.dto;

import java.math.BigDecimal;

public record ProductRequest(
        String productName,
        String description,
        BigDecimal price,
        String sku,
        String imageUrl) {
}
