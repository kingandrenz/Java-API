package com.flex.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flex.orders.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
