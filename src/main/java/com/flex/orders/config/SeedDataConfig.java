package com.flex.orders.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.flex.orders.model.Order;
import com.flex.orders.model.OrderStatus;
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
                repository.save(new Order("Alice", 100.0, OrderStatus.PENDING));
                repository.save(new Order("Bob", 200.0, OrderStatus.SHIPPED));
                repository.save(new Order("Charlie", 150.0, OrderStatus.DELIVERED));
                repository.save(new Order("David", 300.0, OrderStatus.CANCELLED));
                repository.save(new Order("Eve", 120.0, OrderStatus.PENDING));
                repository.save(new Order("Frank", 275.5, OrderStatus.SHIPPED));
                repository.save(new Order("Grace", 60.0, OrderStatus.DELIVERED));
                repository.save(new Order("Hannah", 330.0, OrderStatus.CANCELLED));
                repository.save(new Order("Ian", 410.0, OrderStatus.PENDING));
                repository.save(new Order("Jane", 95.0, OrderStatus.SHIPPED));
                repository.save(new Order("Kyle", 180.0, OrderStatus.DELIVERED));
                repository.save(new Order("Laura", 220.0, OrderStatus.PENDING));
                repository.save(new Order("Matt", 145.0, OrderStatus.SHIPPED));
                repository.save(new Order("Nina", 75.0, OrderStatus.DELIVERED));
                repository.save(new Order("Oscar", 500.0, OrderStatus.CANCELLED));
                repository.save(new Order("Paula", 250.0, OrderStatus.PENDING));
                repository.save(new Order("Quinn", 310.0, OrderStatus.SHIPPED));
                repository.save(new Order("Rita", 130.0, OrderStatus.DELIVERED));
                repository.save(new Order("Sam", 85.0, OrderStatus.PENDING));
                System.out.println("Sample orders added to the database.");
            } else {
                System.out.println("Database already contains orders. Skipping seeding.");
            }
        };
    }
}