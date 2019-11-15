package com.exercise.cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.exercise.cat"})
public class TwoLevelCacheDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoLevelCacheDemoApplication.class, args);
    }

}
