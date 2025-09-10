package com.innowise.sales.customer.analysis.service;

import com.innowise.sales.customer.analysis.dto.Customer;
import com.innowise.sales.customer.analysis.dto.Order;
import com.innowise.sales.customer.analysis.dto.OrderItem;
import com.innowise.sales.customer.analysis.dto.OrderStatus;
import com.innowise.sales.customer.analysis.exception.NoItemsFoundException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderMetricsService {
    private final List<Order> orders;
    private List<OrderItem> cachedDeliveredItems;

    public OrderMetricsService(List<Order> orders) {
        this.orders = Objects.requireNonNullElse(orders, Collections.emptyList());
    }

    public List<String> getUniqueCities() {
        if (orders.isEmpty()) {
            return List.of();
        }

        Set<String> uniqueCities = orders.stream()
                .map(Order::getCustomer)
                .map(Customer::getCity)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (orders.size() == 1) {
            return List.copyOf(uniqueCities);
        }

        return uniqueCities.size() <= 1 ? List.of() : List.copyOf(uniqueCities);
    }

    public double getTotalIncome() {
        return getDeliveredItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public String getTheMostPopularProduct() {
        return orders.stream()
                .filter(order -> isSold(order.getStatus()))
                .map(Order::getItems)
                .flatMap(List::stream)
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
        Map<String, Customer> customerById = orders.stream()
                .map(Order::getCustomer)
                .collect(Collectors.toMap(
                        Customer::getCustomerId,
                        Function.identity(),
                        (c1, c2) -> c1
                ));

        return orders.stream()
                .map(Order::getCustomer)
                .collect(Collectors.groupingBy(
                        Customer::getCustomerId,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(entry -> customerById.get(entry.getKey()))
                .toList();
    }

    private boolean isSold(OrderStatus status) {
        return status == OrderStatus.DELIVERED || status == OrderStatus.SHIPPED;
    }

    private List<OrderItem> getDeliveredItems() {
        if (cachedDeliveredItems == null) {
            cachedDeliveredItems = orders.stream()
                    .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                    .map(Order::getItems)
                    .flatMap(List::stream)
                    .toList();
        }
        return cachedDeliveredItems;
    }

}
