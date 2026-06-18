package com.flex.orders.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flex.orders.dto.OrderRequestDto;
import com.flex.orders.dto.OrderResponseDto;
import com.flex.orders.mapper.OrderMapper;
import com.flex.orders.model.Order;
import com.flex.orders.service.OrderService;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> response = orderService.getAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderMapper.toDto(orderService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        Order order = orderMapper.toEntity(dto);
        Order savedOrder = orderService.addOrder(order);
        return ResponseEntity.status(201).body(orderMapper.toDto(savedOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable int id, @Valid @RequestBody OrderRequestDto dto) {
        Order updatedOrder = orderService.updateOrder(id, orderMapper.toEntity(dto));
        return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
