public class CustomLinkedList<V> {
    private int size = 0;
    private Node<V> first;
    private Node<V> last;

    public int size() {
        return size;
    }

    public boolean addFirst(V value) {
        Node<V> newNode = new Node<>(value);
        newNode.setNext(first);
        if (first != null) {
            first.setPrev(newNode);
        } else {
            last = newNode;
        }
        first = newNode;
        size++;
        return true;
    }

    public boolean addLast(V value) {
        Node<V> newNode = new Node<>(value);
        newNode.setPrev(last);
        if (last != null) {
            last.setNext(newNode);
        } else {
            first = newNode;
        }
        last = newNode;
        size++;
        return true;
    }

    public boolean add(int index, V value) {
        if (index < 0 || index > size) return false;
        if (index == 0) return addFirst(value);
        if (index == size) return addLast(value);

        Node<V> current = getNode(index);
        if (current == null) return false;

        Node<V> newNode = new Node<>(value);
        Node<V> prev = current.getPrev();

        newNode.setPrev(prev);
        newNode.setNext(current);
        if (prev != null) prev.setNext(newNode);
        current.setPrev(newNode);

        size++;
        return true;
    }

    public V getFirst() {
        return first != null ? first.getValue() : null;
    }

    public V getLast() {
        return last != null ? last.getValue() : null;
    }

    public V get(int index) {
        Node<V> node = getNode(index);
        if (node == null) return null;
        return node.getValue();
    }

    public boolean removeFirst() {
        if (first == null) return false;
        first = first.getNext();
        if (first != null) {
            first.setPrev(null);
        } else {
            last = null;
        }
        size--;
        return true;
    }

    public boolean removeLast() {
        if (last == null) return false;
        last = last.getPrev();
        if (last != null) {
            last.setNext(null);
        } else {
            first = null;
        }
        size--;
        return true;
    }

    public boolean remove(int index) {
        if (index < 0 || index >= size) return false;
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();

        Node<V> node = getNode(index);
        if (node == null) return false;

        Node<V> prev = node.getPrev();
        Node<V> next = node.getNext();

        if (prev != null) prev.setNext(next);
        if (next != null) next.setPrev(prev);

        size--;
        return true;
    }

    private Node<V> getNode(int index) {
        if (index < 0 || index >= size) return null;

        Node<V> current;
        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current;
    }
}
