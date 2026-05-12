package com.flex.orders.service;

import com.flex.orders.repository.OrderRepository;
import java.util.List;

import com.flex.orders.exception.OrderNotFoundException;
import com.flex.orders.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(int id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(int id, Order updated) {
        Order existing = getById(id);
        existing.setCustomerName(updated.getCustomerName());
        existing.setAmount(updated.getAmount());
        existing.setStatus(updated.getStatus());
        return orderRepository.save(existing);
    }

    public void deleteOrder(int id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }
}
