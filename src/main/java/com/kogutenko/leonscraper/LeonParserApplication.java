package com.kogutenko.leonscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LeonParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeonParserApplication.class, args);
    }
} 