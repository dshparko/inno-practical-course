import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class TestDataFactory {

    public static Customer createCustomer(String id, String name, String email, String city, int age) {
        return new Customer(id, name, email, LocalDateTime.now(), age, city);
    }

    public static Customer createDefaultCustomer() {
        return createCustomer("0", "default", "default@mail.com", "Minsk", 30);
    }

    public static List<Customer> createSampleCustomers() {
        return List.of(
                createCustomer("1", "Darya", "darya@mail.com", "Minsk", 22),
                createCustomer("2", "Ivan", "ivan@mail.com", "Pinsk", 52),
                createCustomer("3", "Anna", "anna@mail.com", "Grodno", 35),
                createCustomer("4", "Sergey", "sergey@mail.com", "Brest", 41)
        );
    }

    public static List<OrderItem> createOrderItems(String product1, String product2) {
        return List.of(
                new OrderItem(product1, 10, 40.3, Category.HOME),
                new OrderItem(product2, 2, 2000, Category.ELECTRONICS)
        );
    }

    public static List<OrderItem> createBooksAndStationery() {
        return List.of(
                new OrderItem("Book", 5, 20, Category.BOOKS),
                new OrderItem("Pen", 10, 3.5, Category.HOME)
        );
    }

    public static List<OrderItem> createMixedItems() {
        return List.of(
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

    public static List<Order> createMixedOrders() {
        List<Customer> customers = createSampleCustomers();
        List<OrderItem> electronics = createOrderItems("Phone", "Laptop");
        List<OrderItem> books = createBooksAndStationery();
        List<OrderItem> mixed = createMixedItems();

        return List.of(
                createOrder("101", customers.get(0), electronics, OrderStatus.NEW),
                createOrder("102", customers.get(1), books, OrderStatus.DELIVERED),
                createOrder("103", customers.get(2), mixed, OrderStatus.PROCESSING),
                createOrder("104", customers.get(3), electronics, OrderStatus.DELIVERED),
                createOrder("105", customers.get(0), books, OrderStatus.CANCELLED),
                createOrder("106", customers.get(1), mixed, OrderStatus.DELIVERED)
        );
    }
}
