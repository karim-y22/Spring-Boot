package at.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class SpringBootWebApp {
    public static void main(String[] args) {
        SpringApplication.run(at.boot.SpringBootWebApp.class, args);
    }
}
