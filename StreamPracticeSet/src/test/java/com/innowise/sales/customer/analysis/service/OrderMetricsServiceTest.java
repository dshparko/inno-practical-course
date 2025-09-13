package com.innowise.sales.customer.analysis.service;

import com.innowise.sales.customer.analysis.dto.Customer;
import com.innowise.sales.customer.analysis.exception.NoItemsFoundException;
import com.innowise.sales.customer.analysis.util.testdata.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class OrderMetricsServiceTest {
    @Test
    @DisplayName("Should return unique cities from mixed orders")
    void shouldReturnUniqueCitiesFromMixedOrders() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createMixedOrders());
        List<String> cities = metrics.getUniqueCities();

        assertEquals(4, cities.size());
        assertTrue(cities.contains("Minsk"));
        assertTrue(cities.contains("Pinsk"));
        assertTrue(cities.contains("Grodno"));
        assertTrue(cities.contains("Brest"));
    }

    @Test
    @DisplayName("Should return one city from 1 order with the same cities")
    void shouldReturnOneCityForGetUniqueCities() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createOrderWithOneCity());
        List<String> result = metrics.getUniqueCities();

        assertEquals(1, result.size());
        assertTrue(result.contains("Minsk"));
    }


    @Test
    @DisplayName("Should return empty list from mixed orders with the same cities")
    void shouldReturnEmptyListForGetUniqueCities() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createMixedOrdersWithSameCity());
        List<String> result = metrics.getUniqueCities();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list without orders")
    void shouldReturnEmptyListForGetUniqueCitiesWithoutOrders() {
        OrderMetricsService metrics = new OrderMetricsService(List.of());
        List<String> result = metrics.getUniqueCities();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should calculate total income from delivered orders only")
    void shouldCalculateTotalIncomeFromDeliveredOrdersOnly() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createMixedOrders());
        double income = metrics.getTotalIncome();

        double expected = (100 + 100) + (5 * 20 + 10 * 3.5) + (150 + 2 * 300 + 3 * 15);
        assertEquals(expected, income);
    }

    @Test
    @DisplayName("Should identify most popular product across all orders")
    void shouldIdentifyMostPopularProductAcrossAllOrders() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createMixedOrders());
        String popular = metrics.getTheMostPopularProduct();

        assertTrue(popular.equals("Phone") || popular.equals("Laptop"));
    }

    @Test
    @DisplayName("Should return exception")
    void shouldReturnMessageForGetTheMostPopularProductAcrossAllOrders() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createOrdersWithoutDeliveredStatus());

        assertThrows(NoItemsFoundException.class, metrics::getTheMostPopularProduct);
    }

    @Test
    @DisplayName("Should calculate average check for delivered orders")
    void shouldCalculateAverageCheckForDeliveredOrders() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createMixedOrders());

        double total = 135 + 200 + 795;
        int totalItems = 2 + 2 + 3;
        double expectedAvg = total / totalItems;

        assertEquals(expectedAvg, metrics.getAverageCheckForDeliveredOrders());
    }

    @Test
    @DisplayName("Should return 0 for average check for delivered orders")
    void returnsZeroWhenNoDeliveredItemsPresent() {

        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createOrdersWithoutDeliveredStatus());
        double result = metrics.getAverageCheckForDeliveredOrders();

        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Should return empty list for mixedOrders")
    void shouldReturnEmptyListForGetCustomersWithMoreThan5Orders() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createMixedOrders());
        List<Customer> result = metrics.getCustomersWithMoreThanXOrders(5L);

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should return customer with >5 orders")
    @Test
    void testCustomerWithMoreThanFiveOrders() {
        OrderMetricsService metrics = new OrderMetricsService(TestDataFactory.createOrdersWithFrequentCustomer());
        List<Customer> result = metrics.getCustomersWithMoreThanXOrders(5L);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(c -> c.getName().equals("Darya")));
    }
}