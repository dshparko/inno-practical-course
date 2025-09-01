import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;
}