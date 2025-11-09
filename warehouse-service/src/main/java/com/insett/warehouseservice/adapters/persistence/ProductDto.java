package com.insett.warehouseservice.adapters.persistence;

import java.math.BigDecimal;

public record ProductDto(String name, String description, BigDecimal price, String imageUrl) {
}
