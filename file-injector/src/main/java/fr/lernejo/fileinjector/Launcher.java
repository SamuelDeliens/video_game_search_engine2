package fr.lernejo.fileinjector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(Launcher.class)) {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, GameInfo.class);
            List<GameInfo> gameInfoList = objectMapper.readValue(new File(args[0]), listType);
            RabbitTemplate rabbitTemplate = ctx.getBean(RabbitTemplate.class);
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
            for (GameInfo gameInfo : gameInfoList) {
                rabbitTemplate.convertAndSend("game_info", gameInfo, message -> {
                    message.getMessageProperties().setHeader("game_id", gameInfo.id());
                    message.getMessageProperties().setContentType("application/json");
                    return message;
                });
            }
            SpringApplication.run(Launcher.class, args);
        } catch (Exception e) { System.exit(1); }
    }
}
