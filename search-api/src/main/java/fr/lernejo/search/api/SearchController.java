package fr.lernejo.search.api;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    private final RestHighLevelClient client;

    public SearchController(RestHighLevelClient client) {
        this.client = client;
    }

    @GetMapping("/api/games?query={query}")
    List<Map<String, Object>> searchGame(@PathVariable String query, @RequestParam(defaultValue = "5") int size) {
        SearchRequest searchRequest = new SearchRequest("games");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(query);
        searchSourceBuilder.query(queryStringQueryBuilder);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        try {
            var searchHits = client.search(searchRequest, RequestOptions.DEFAULT).getHits();
            return Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error executing ElasticSearch query", e);
        }
    }

}
