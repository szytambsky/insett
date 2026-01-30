package com.insett.indicesservice.api.controller;

import com.insett.indicesservice.api.dto.SuggestionRequestParameters;
import com.insett.indicesservice.api.dto.SuggestionsResponse;
import com.insett.indicesservice.domain.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping("/api")
public class SearchForSuggestionController {

    private final SuggestionService suggestionService;

    /**
     * Handles GET requests for suggestion strings based on the provided request parameters.
     *
     * @param request parameters bound from the incoming request used to fetch suggestions
     * @return a ResponseEntity containing a SuggestionsResponse with the list of suggestion strings and HTTP 200 status
     */
    @GetMapping("/suggestions")
    public ResponseEntity<SuggestionsResponse> searchSuggestions(@ModelAttribute SuggestionRequestParameters request) {
        List<String> suggestions = suggestionService.fetchSuggestions(request);
        SuggestionsResponse response = new SuggestionsResponse(suggestions);
        return ResponseEntity.ok(response);
    }
}