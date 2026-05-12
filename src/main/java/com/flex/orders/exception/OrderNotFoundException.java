package com.flex.orders.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(int id) {
        super("Order not found with ID: " + id);
    }

}
