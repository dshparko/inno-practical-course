import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MetricsTest {
    @Test
    @DisplayName("Should return unique cities from mixed orders")
    void shouldReturnUniqueCitiesFromMixedOrders() {
        Metrics metrics = new Metrics(TestDataFactory.createMixedOrders());
        List<String> cities = metrics.getUniqueCities();

        assertEquals(4, cities.size());
        assertTrue(cities.contains("Minsk"));
        assertTrue(cities.contains("Pinsk"));
        assertTrue(cities.contains("Grodno"));
        assertTrue(cities.contains("Brest"));
    }

    @Test
    @DisplayName("Should return empty list from mixed orders with the same cities")
    void shouldReturnEmptyListForGetUniqueCities() {
        Metrics metrics = new Metrics(TestDataFactory.createMixedOrdersWithSameCity("Minsk"));
        List<String> result = metrics.getUniqueCities();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should calculate total income from delivered orders only")
    void shouldCalculateTotalIncomeFromDeliveredOrdersOnly() {
        Metrics metrics = new Metrics(TestDataFactory.createMixedOrders());
        double income = metrics.getTotalIncome();

        double expected = (100 + 100) + (5 * 20 + 10 * 3.5) + (150 + 2 * 300 + 3 * 15);
        assertEquals(expected, income);
    }

    @Test
    @DisplayName("Should identify most popular product across all orders")
    void shouldIdentifyMostPopularProductAcrossAllOrders() {
        Metrics metrics = new Metrics(TestDataFactory.createMixedOrders());
        String popular = metrics.getTheMostPopularProduct();

        assertTrue(popular.equals("Phone") || popular.equals("Laptop"));
    }

    @Test
    @DisplayName("Should return message")
    void shouldReturnMessageForGetTheMostPopularProductAcrossAllOrders() {
        Metrics metrics = new Metrics(TestDataFactory.createOrdersWithoutProducts());
        String popular = metrics.getTheMostPopularProduct();

        assertEquals("No products found", popular);
    }

    @Test
    @DisplayName("Should calculate average check for delivered orders")
    void shouldCalculateAverageCheckForDeliveredOrders() {
        Metrics metrics = new Metrics(TestDataFactory.createMixedOrders());

        double total = 135 + 200 + 795;
        int totalItems = 2 + 2 + 3;
        double expectedAvg = total / totalItems;

        assertEquals(expectedAvg, metrics.getAverageCheckForDeliveredOrders());
    }

    @Test
    @DisplayName("Should return 0 for average check for delivered orders")
    void returnsZeroWhenNoDeliveredItemsPresent() {

        Metrics metrics = new Metrics(TestDataFactory.createOrdersWithoutDeliveredStatus());
        double result = metrics.getAverageCheckForDeliveredOrders();

        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Should return empty list for mixedOrders")
    void shouldReturnEmptyListForGetCustomersWithMoreThan5Orders() {
        Metrics metrics = new Metrics(TestDataFactory.createMixedOrders());
        List<Customer> result = metrics.getCustomersWithMoreThan5Orders();

        assertTrue(result.isEmpty());
    }

    @DisplayName("Should return customer with >5 orders")
    @Test
    void testCustomerWithMoreThanFiveOrders() {
        Metrics metrics = new Metrics(TestDataFactory.createOrdersWithFrequentCustomer());
        List<Customer> result = metrics.getCustomersWithMoreThan5Orders();

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(c -> c.getName().equals("Darya")));
    }
}