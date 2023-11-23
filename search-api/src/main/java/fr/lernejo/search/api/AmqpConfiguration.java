package fr.lernejo.search.api;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

    static final String GAME_INFO_QUEUE = "game_info";

    @Bean
    Queue queue() {
        return new Queue(GAME_INFO_QUEUE, true);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(GameInfoListener gameInfoListener) {
        return new MessageListenerAdapter(gameInfoListener, "onMessage");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(GAME_INFO_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
