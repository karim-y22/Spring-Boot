package at.boot;

import at.boot.banner.DevBanner;
import at.boot.banner.ProdBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootWebApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootWebApp.class);

        String activeProfile = System.getProperty("spring.profiles.active", "default");

        if ("dev".equals(activeProfile)) {
            app.setBanner(new DevBanner());
        } else if ("prod".equals(activeProfile)) {
            app.setBanner(new ProdBanner());
        }
        var context = app.run(args);
    }
}