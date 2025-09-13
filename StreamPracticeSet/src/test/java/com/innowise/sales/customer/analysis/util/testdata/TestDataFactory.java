package com.innowise.sales.customer.analysis.util.testdata;

import com.innowise.sales.customer.analysis.dto.Category;
import com.innowise.sales.customer.analysis.dto.Customer;
import com.innowise.sales.customer.analysis.dto.Order;
import com.innowise.sales.customer.analysis.dto.OrderItem;
import com.innowise.sales.customer.analysis.dto.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class TestDataFactory {

    private static final List<Customer> customers = createSampleCustomers();
    private static final List<Customer> customersWithTheSameCity = createSampleCustomersWithSameCity("Minsk");


    public static Customer createCustomer(String id, String name, String email, String city, int age) {
        return new Customer(id, name, email, LocalDateTime.now(), age, city);
    }

    public static List<Customer> createSampleCustomers() {
        return Arrays.asList(
                createCustomer("1", "Darya", "darya@mail.com", "Minsk", 22),
                createCustomer("2", "Ivan", "ivan@mail.com", "Pinsk", 52),
                createCustomer("3", "Anna", "anna@mail.com", "Grodno", 35),
                createCustomer("4", "Sergey", "sergey@mail.com", "Brest", 41)
        );
    }

    public static List<Customer> createSampleCustomersWithSameCity(String city) {
        return IntStream.rangeClosed(1, 4)
                .mapToObj(i -> createCustomer(String.valueOf(i), "User" + i, "user" + i + "@mail.com", city, 30 + i))
                .toList();
    }

    public static List<OrderItem> createOrderItems(String... productNames) {
        List<OrderItem> items = new ArrayList<>();
        for (String name : productNames) {
            items.add(new OrderItem(name, 1, 100, Category.HOME));
        }
        return items;
    }

    public static List<OrderItem> createBooksAndStationery() {
        return Arrays.asList(
                new OrderItem("Book", 5, 20, Category.BOOKS),
                new OrderItem("Pen", 10, 3.5, Category.HOME)
        );
    }

    public static List<OrderItem> createMixedItems() {
        return Arrays.asList(
                new OrderItem("Chair", 1, 150, Category.HOME),
                new OrderItem("Monitor", 2, 300, Category.ELECTRONICS),
                new OrderItem("Notebook", 3, 15, Category.BOOKS)
        );
    }


    public static Order createOrder(String id, Customer customer, List<OrderItem> items, OrderStatus status) {
        return new Order(id, LocalDateTime.now(), customer, items, status);
    }

    public static List<Order> createOrders(Customer customer, List<OrderItem> items, OrderStatus status, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createOrder(String.valueOf(i), customer, items, status))
                .toList();
    }


    public static List<Order> createOrdersWithoutDeliveredStatus() {
        return List.of(
                createOrder("1", customers.get(0), createOrderItems("Phone", "Laptop"), OrderStatus.NEW),
                createOrder("2", customers.get(2), createOrderItems("Phone", "Laptop"), OrderStatus.CANCELLED)
        );
    }

    public static List<Order> createMixedOrders() {
        return Arrays.asList(
                createOrder("101", customers.get(0), createOrderItems("Phone", "Laptop"), OrderStatus.NEW),
                createOrder("102", customers.get(1), createBooksAndStationery(), OrderStatus.DELIVERED),
                createOrder("103", customers.get(2), createMixedItems(), OrderStatus.PROCESSING),
                createOrder("104", customers.get(3), createOrderItems("Phone", "Laptop"), OrderStatus.DELIVERED),
                createOrder("105", customers.get(0), createBooksAndStationery(), OrderStatus.CANCELLED),
                createOrder("106", customers.get(1), createMixedItems(), OrderStatus.DELIVERED)
        );
    }

    public static List<Order> createMixedOrdersWithSameCity() {
        return Arrays.asList(
                createOrder("101", customersWithTheSameCity.get(0), createOrderItems("Phone", "Laptop"), OrderStatus.NEW),
                createOrder("102", customersWithTheSameCity.get(1), createBooksAndStationery(), OrderStatus.DELIVERED),
                createOrder("103", customersWithTheSameCity.get(2), createMixedItems(), OrderStatus.PROCESSING),
                createOrder("104", customersWithTheSameCity.get(3), createOrderItems("Phone", "Laptop"), OrderStatus.DELIVERED),
                createOrder("105", customersWithTheSameCity.get(0), createBooksAndStationery(), OrderStatus.CANCELLED),
                createOrder("106", customersWithTheSameCity.get(1), createMixedItems(), OrderStatus.DELIVERED)
        );
    }

    public static List<Order> createOrderWithOneCity() {
        return List.of(
                createOrder("101", customersWithTheSameCity.getFirst(), createOrderItems("Phone", "Laptop"), OrderStatus.NEW)
        );
    }

    public static List<Order> createOrdersWithFrequentCustomer() {
        Customer frequentCustomer = createCustomer("99", "Darya", "darya@mail.com", "Minsk", 22);
        List<Order> mixedOrders = new ArrayList<>(createMixedOrders());
        mixedOrders.addAll(createOrders(frequentCustomer, createBooksAndStationery(), OrderStatus.DELIVERED, 6));
        return mixedOrders;
    }
}
