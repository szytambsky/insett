package com.insett.indicesservice.suggestions;

import com.insett.indicesservice.AbstractIndicesServiceTests;
import com.insett.indicesservice.api.dto.SuggestionsResponse;
import com.insett.indicesservice.domain.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import tools.jackson.core.type.TypeReference;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@AutoConfigureTestRestTemplate
public class SuggestionTest extends AbstractIndicesServiceTests {

    public static final Logger log = LoggerFactory.getLogger(SuggestionTest.class);
    private static final String API_PATH = "/api/suggestions?%s";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    public void setupAll() {
        Map<String, Object> indexMapping = super.readResource("test-data/suggestion-index-mapping.json",
                new TypeReference<Map<String, Object>>() {});
        List<Object> suggestionData = super.readResource("test-data/suggestion-data.json",
                new TypeReference<List<Object>>() {});
        IndexOperations indexOperations = elasticsearchOperations.indexOps(Constants.Index.SUGGESTION);
        Document document = Document.from(indexMapping);
        indexOperations.create(Collections.emptyMap(), document);
        elasticsearchOperations.withRefreshPolicy(RefreshPolicy.IMMEDIATE).save(suggestionData, Constants.Index.SUGGESTION);
        SearchHits<Object> searchHits = elasticsearchOperations.search(elasticsearchOperations.matchAllQuery(), Object.class, Constants.Index.SUGGESTION);
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    @ParameterizedTest
    @MethodSource("successTestData")
    public void suggestionSuccessTest(String parameters, List<String> expectedResults) {
        var path = API_PATH.formatted(parameters);
        var responseEntity = this.restTemplate.exchange(
                RequestEntity.get(URI.create(path)).build(),
                new ParameterizedTypeReference<SuggestionsResponse>() {
                }
        );
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        log.info("response: {}", responseEntity.getBody());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(expectedResults, responseEntity.getBody().suggestions());

    }

    private Stream<Arguments> successTestData() {
        return Stream.of(
                Arguments.of("prefix=w", List.of("walmart")),
                Arguments.of("prefix=c", List.of("cafe", "coffee")),
                Arguments.of("prefix=c&limit=1", List.of("cafe")),
                Arguments.of("prefix=co", List.of("coffee")),
                Arguments.of("prefix=cofe", List.of("coffee")), // fuzzy - but not cafe because of fixed prefix 2
                Arguments.of("prefix=cffee", List.of()), // fuzzy prefix length 2, nothing starting from fixed cf
                Arguments.of("prefix=12", List.of()),
                Arguments.of("prefix=x", List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("failureTestData")
    public void suggestionsFailureTest(String parameters){
        var path = API_PATH.formatted(parameters);
        var responseEntity = this.restTemplate.getForEntity(URI.create(path), ProblemDetail.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals("prefix can not be empty", responseEntity.getBody().getDetail());
    }

    private static Stream<Arguments> failureTestData() {
        return Stream.of(
                Arguments.of("prefix="),
                Arguments.of("")
        );
    }
}
