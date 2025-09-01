import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Metrics {
    private List<Order> orders;

    public Metrics(List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }

    public List<String> getUniqueCities() {
        return orders.stream()
                .map(Order::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getCity)
                .distinct()
                .toList();
    }

    public double getTotalIncome() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getItems)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }

    public String getTheMostPopularProduct() {
        return orders.stream()
                .map(Order::getItems)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No products found");
    }
}
