package fr.lernejo.search.api;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration {

    @Bean
    public RestHighLevelClient createElasticSearchClient(
        @Value("${elasticsearch.host:localhost}") String host,
        @Value("${elasticsearch.port:9200}") int port,
        @Value("${elasticsearch.username:elastic}") String username,
        @Value("${elasticsearch.password:admin}") String password
    ) {
        return new RestHighLevelClient(
            RestClient.builder(new HttpHost(host, port, "http"))
                .setHttpClientConfigCallback(new HttpClientConfigCallback() {
                     @Override
                     public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                         final BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
                         basicCredentialsProvider.setCredentials(
                             AuthScope.ANY,
                             new UsernamePasswordCredentials(username, password)
                         );
                         return httpAsyncClientBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
                     }
                 })
        );

    }

}
