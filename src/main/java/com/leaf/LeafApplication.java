package com.leaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LeafApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class, args);
    }

}
