import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Metrics {
    private final List<Order> orders;

    public Metrics(List<Order> orders) {
        this.orders = Objects.requireNonNullElse(orders, Collections.emptyList());
    }

    public List<String> getUniqueCities() {
        Set<String> uniqueCities = orders.stream()
                .map(Order::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getCity)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return uniqueCities.size() <= 1 ? List.of() : new ArrayList<>(uniqueCities);
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
                .orElse("No products found");
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
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<OrderItem> getDeliveredItems() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getItems)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .toList();
    }

    private List<OrderItem> getAllItems() {
        return orders.stream()
                .map(Order::getItems)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .toList();
    }
}
