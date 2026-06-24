package com.flex.orders.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.flex.orders.exception.OrderNotFoundException;
import com.flex.orders.model.Order;
import com.flex.orders.model.OrderStatus;
import com.flex.orders.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnAllOrders() {
        Order order1 = new Order("Alice", 100.0, OrderStatus.PENDING);
        Order order2 = new Order("Bob", 200.0, OrderStatus.CANCELLED);

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order1, order2)));

        Page<Order> result = orderService.getAll(Pageable.unpaged());
        assertEquals(2, result.getContent().size());
        assertEquals("Alice", result.getContent().get(0).getCustomerName());
        assertEquals("Bob", result.getContent().get(1).getCustomerName());
    }

    @Test
    void shouldReturnOrderById() {
        Order order = new Order("Charlie", 150.0, OrderStatus.DELIVERED);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.getById(1);
        assertEquals("Charlie", result.getCustomerName());
        assertEquals(150.0, result.getAmount());
        assertEquals(OrderStatus.DELIVERED, result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getById(999));

    }

    @Test
    void shouldAddOrderAndReturnSavedOrder() {
        Order order = new Order("David", 250.0, OrderStatus.PENDING);

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.addOrder(order);
        assertEquals("David", result.getCustomerName());
        assertEquals(250.0, result.getAmount());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldDeleteOrderWhenOrderExists() {
        when(orderRepository.existsById(1)).thenReturn(true);

        orderService.deleteOrder(1);

        verify(orderRepository).existsById(1);
        verify(orderRepository).deleteById(1);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenDeletingNonExistingOrder() {
        when(orderRepository.existsById(999)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(999));

        verify(orderRepository).existsById(999);
    }

    @Test
    void shouldUpdateOrderWhenOrderExists() {
        Order existingOrder = new Order("Eve", 300.0, OrderStatus.PENDING);
        Order updatedOrder = new Order("Eve", 350.0, OrderStatus.DELIVERED);

        when(orderRepository.findById(1)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1, updatedOrder);

        assertEquals("Eve", result.getCustomerName());
        assertEquals(350.0, result.getAmount());
        assertEquals(OrderStatus.DELIVERED, result.getStatus());

        verify(orderRepository).findById(1);
        verify(orderRepository).save(existingOrder);
    }

}