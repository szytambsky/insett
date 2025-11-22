package com.insett.warehouseservice.adapters.api.dto;

import java.math.BigDecimal;

public record ProductDto(String name, String description, BigDecimal price, String imageUrl) {
}
