package com.insett.indicesservice.api.dto.search;

public record Pagination(int page,
                         int size,
                         long totalElements,
                         int totalPages) {
}
