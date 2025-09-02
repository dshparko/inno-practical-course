import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Node<V> {
    V value;
    Node<V> next;
    Node<V> prev;

    public Node(V value) {
        this.value = value;
        this.next = null;
        this.prev = null;
    }

}
