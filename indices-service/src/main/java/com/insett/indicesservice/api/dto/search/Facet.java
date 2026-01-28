package com.insett.indicesservice.api.dto.search;

import java.util.List;

public record Facet(String name,
                    List<FacetItem> items) {
}
