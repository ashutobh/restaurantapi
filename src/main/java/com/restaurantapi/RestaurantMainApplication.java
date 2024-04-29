package com.restaurantapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = SqlInitializationAutoConfiguration.class)
@EnableAutoConfiguration
@Configuration
@ComponentScan({"com.restaurantapi.controller", "com.restaurantapi.service"})
@EntityScan("com.restaurantapi.entity")
@EnableJpaRepositories("com.restaurantapi.repository")
public class RestaurantMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantMainApplication.class, args);
    }
}