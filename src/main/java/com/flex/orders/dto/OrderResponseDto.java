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

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
