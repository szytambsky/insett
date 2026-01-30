package com.insett.indicesservice.api.controller;

import com.insett.indicesservice.api.dto.search.SearchRequestParameters;
import com.insett.indicesservice.api.dto.search.SearchResponse;
import com.insett.indicesservice.domain.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessSearchController {

    private final SearchService searchService;

    /**
     * Handles GET /api/search requests and performs a business search using the provided parameters.
     *
     * @param parameters search request parameters bound from the HTTP query string
     * @return the search results wrapped in an HTTP 200 (OK) response
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(SearchRequestParameters parameters) {
        SearchResponse searchResponse = searchService.search(parameters);
        return ResponseEntity.ok(searchResponse);
    }
}