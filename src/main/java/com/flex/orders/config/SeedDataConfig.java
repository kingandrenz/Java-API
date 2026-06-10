package com.flex.orders.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.flex.orders.model.Order;
import com.flex.orders.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;

@Configuration
@Profile("dev")
public class SeedDataConfig {
    @Bean
    CommandLineRunner seedData(OrderRepository repository) {
        return (args) -> {
            if (repository.count() == 0) {
                // Sample data
                repository.save(new Order("Alice", 100.0, "PENDING"));
                repository.save(new Order("Bob", 200.0, "SHIPPED"));
                repository.save(new Order("Charlie", 150.0, "DELIVERED"));
                repository.save(new Order("David", 300.0, "CANCELLED"));
                System.out.println("Sample orders added to the database.");
            } else {
                System.out.println("Database already contains orders. Skipping seeding.");
            }
        };
    }
}