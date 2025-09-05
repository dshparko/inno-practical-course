package com.innowise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class Order {
    private String orderId;
    private LocalDateTime orderDate;

    @NonNull
    private Customer customer;


    private List<OrderItem> items;
    private OrderStatus status;

}
