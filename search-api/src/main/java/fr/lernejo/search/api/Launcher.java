package fr.lernejo.search.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(Launcher.class);

        while (true) {}
    }
}
