package com.flex.orders.dto;

public class OrderResponseDto {
    private int id;
    private String customerName;
    private double amount;
    private String status;

    public OrderResponseDto() {
    }

    public OrderResponseDto(int id, String customerName, double amount, String status) {
        this.id = id;
        this.customerName = customerName;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
