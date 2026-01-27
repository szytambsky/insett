package com.insett.indicesservice.domain.util;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

public class ElasticsearchUtil {

    public static Suggester buildCompletionSuggester(String suggestName, String field, String prefix, int limit) {
        SuggestFuzziness suggestFuzziness = SuggestFuzziness.of(suggFuzzBuilder -> suggFuzzBuilder.fuzziness(Constants.Fuzziness.LEVEL)
                .prefixLength(Constants.Fuzziness.PREFIX_LENGTH));
        CompletionSuggester completionSuggester = CompletionSuggester.of(complBuilder -> complBuilder.field(field)
                .size(limit)
                .fuzzy(suggestFuzziness)
                .skipDuplicates(true));
        FieldSuggester fieldSuggester = FieldSuggester.of(fieldSuggBuilder -> fieldSuggBuilder.prefix(prefix)
                .completion(completionSuggester));
        return Suggester.of(suggBuilder -> suggBuilder.suggesters(suggestName, fieldSuggester));

    }
}
