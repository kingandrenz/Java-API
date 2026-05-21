package com.flex.orders.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flex.orders.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
