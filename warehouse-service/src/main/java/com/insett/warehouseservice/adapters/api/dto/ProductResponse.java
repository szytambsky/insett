package com.insett.warehouseservice.adapters.api.dto;

import java.math.BigDecimal;

public record ProductResponse(String name, String description, BigDecimal price) {
}
