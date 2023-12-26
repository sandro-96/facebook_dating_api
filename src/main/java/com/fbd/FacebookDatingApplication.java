package com.fbd;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Log4j2
@RestController
public class FacebookDatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacebookDatingApplication.class, args);
    }
}
