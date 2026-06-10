package com.flex.orders.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;

import com.flex.orders.model.Order;
import com.flex.orders.repository.OrderRepository;
import com.flex.orders.exception.OrderNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnAllOrders() {
        Order order1 = new Order("Alice", 100.0, "PENDING");
        Order order2 = new Order("Bob", 200.0, "CANCELLED");

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> result = orderService.getAll();
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getCustomerName());
        assertEquals("Bob", result.get(1).getCustomerName());
    }

    @Test
    void shouldReturnOrderById() {
        Order order = new Order("Charlie", 150.0, "DELIVERED");
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.getById(1);
        assertEquals("Charlie", result.getCustomerName());
        assertEquals(150.0, result.getAmount());
        assertEquals("DELIVERED", result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getById(999));

    }

    @Test
    void shouldAddOrderAndReturnSavedOrder() {
        Order order = new Order("David", 250.0, "PENDING");
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.addOrder(order);
        assertEquals("David", result.getCustomerName());
        assertEquals(250.0, result.getAmount());
        assertEquals("PENDING", result.getStatus());
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
        Order existingOrder = new Order("Eve", 300.0, "PENDING");
        Order updatedOrder = new Order("Eve", 350.0, "DELIVERED");

        when(orderRepository.findById(1)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1, updatedOrder);

        assertEquals("Eve", result.getCustomerName());
        assertEquals(350.0, result.getAmount());
        assertEquals("DELIVERED", result.getStatus());

        verify(orderRepository).findById(1);
        verify(orderRepository).save(existingOrder);
    }

}