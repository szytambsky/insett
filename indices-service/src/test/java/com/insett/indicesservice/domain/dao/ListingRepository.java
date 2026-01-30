package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Listing;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Query -> { "query" : .... }
 * Do NOT provide query like this: { "query": { "match": {..}}}
 * Instead, provide only this { "match": {..}}
 * Parameters reference:
 *   ?0, ?1 --> refers to the first and second parameters respectively
 *   #{#paramter-name} --> we can also use the method parameter name to access
 * **/

@Repository
public interface ListingRepository extends ElasticsearchRepository<Listing, String> {

    /**
     * Searches Listing documents using a multi-field fuzzy match and returns matching hits with name highlights.
     *
     * Performs a search against the listing's searchable fields (name and brand) using the provided query text.
     *
     * @param query the search text used to match Listing fields
     * @return SearchHits containing the matching Listing documents; highlight fragments for the `name` field are included when available
     */
    @Query("""
            {
                "multi_match": {
                  "query": "#{#query}",
                  "fields": [ "name^3", "brand"],
                  "operator": "and",
                  "fuzziness": 1,
                  "type": "best_fields",
                  "tie_breaker": 0.7
                }
            }
            """)
    @Highlight(fields = {
            @HighlightField(
                    name = "name",
                    parameters = @HighlightParameters(
                            preTags = "<b>",
                            postTags = "</b>"
                    )
            )
    })
    SearchHits<Listing> search(String query);
}