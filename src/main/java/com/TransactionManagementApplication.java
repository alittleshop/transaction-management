package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TransactionManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionManagementApplication.class, args);
    }

}
