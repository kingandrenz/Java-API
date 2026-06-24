package com.flex.orders.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class OrderRequestDto {
    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String customerName;

    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private double amount;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PENDING|COMPLETED|CANCELLED", message = "Status must be PENDING, SHIPPED, DELIVERED, or CANCELLED")
    @Column(nullable = false)
    private String status;

    public OrderRequestDto() {
    }

    public OrderRequestDto(String customerName, double amount, String status) {
        this.customerName = customerName;
        this.amount = amount;
        this.status = status;
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
