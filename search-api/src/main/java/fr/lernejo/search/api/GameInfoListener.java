package fr.lernejo.search.api;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static fr.lernejo.search.api.AmqpConfiguration.GAME_INFO_QUEUE;

@Component
public class GameInfoListener {

    private final RestHighLevelClient client;

    public GameInfoListener(RestHighLevelClient client) {
        this.client = client;
    }

    @RabbitListener(queues = GAME_INFO_QUEUE)
    public void onMessage(String message, @Header("game_id") String gameId) {
        System.out.println("Received message: " + message);

        try {
            IndexRequest indexRequest = new IndexRequest("games")
                .id(gameId)
                .source(message, XContentType.JSON);

            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

            System.out.println("Document indexed successfully. Index: " + indexResponse.getIndex() + ", ID: " + indexResponse.getId());

        } catch (Exception e) {
            System.err.println("Error indexing document: " + e.getMessage());
        }
    }
}
