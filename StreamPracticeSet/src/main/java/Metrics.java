import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

}
