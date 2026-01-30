package com.insett.indicesservice.api.dto.search;

import java.util.List;

public record SearchResponse(List<Business> results,
                             List<Facet> facets,
                             Pagination pagination,
                             long timeTaken) {
}
