package com.innowise.service;

import com.innowise.dto.Customer;
import com.innowise.dto.Order;
import com.innowise.dto.OrderItem;
import com.innowise.dto.OrderStatus;
import com.innowise.exception.NoItemsFoundException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderMetricsService {
    private final List<Order> orders;
    private List<OrderItem> cachedDeliveredItems;
    private List<OrderItem> cachedAllItems;

    public OrderMetricsService(List<Order> orders) {
        this.orders = Objects.requireNonNullElse(orders, Collections.emptyList());
    }

    public List<String> getUniqueCities() {
        Set<String> uniqueCities = orders.stream()
                .map(Order::getCustomer)
                .map(Customer::getCity)
                .collect(Collectors.toSet());

        return uniqueCities.size() <= 1 && orders.size() != 1 ? List.of() : new ArrayList<>(uniqueCities);
    }

    public double getTotalIncome() {
        return getDeliveredItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public String getTheMostPopularProduct() {
        return getAllItems().stream()
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(NoItemsFoundException::new);
    }

    public double getAverageCheckForDeliveredOrders() {
        return getDeliveredItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .average()
                .orElse(0);
    }

    public List<Customer> getCustomersWithMoreThan5Orders() {
        return orders.stream()
                .map(Order::getCustomer)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<OrderItem> getDeliveredItems() {
        if (cachedDeliveredItems == null) {
            cachedDeliveredItems = orders.stream()
                    .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                    .map(Order::getItems)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .toList();
        }
        return cachedDeliveredItems;
    }

    private List<OrderItem> getAllItems() {
        if (cachedAllItems == null) {
            cachedAllItems = orders.stream()
                    .map(Order::getItems)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .toList();
        }
        return cachedAllItems;
    }
}
