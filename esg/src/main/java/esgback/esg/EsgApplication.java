package esgback.esg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EsgApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsgApplication.class, args);
    }

}
