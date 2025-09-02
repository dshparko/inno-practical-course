import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {
    @Test
    @DisplayName("Should add first element")
    void testAddFirstAndGetFirst() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addFirst("B");
        list.addFirst("A");

        assertEquals("A", list.getFirst());
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Should add last element")
    void testAddLastAndGetLast() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.addLast("B");

        assertEquals("B", list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Should add first element by index")
    void shouldAddByIndex() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.addLast("C");
        list.add(1, "B");
        list.add(3, "D");

        assertEquals("B", list.get(1));
        assertEquals("D", list.get(3));
        assertEquals(4, list.size());
    }

    @Test
    @DisplayName("Should get proper value by index")
    void shouldGetProperValueByIndex() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);

        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
        assertNull(list.get(5));
    }

    @Test
    @DisplayName("Should remove first element")
    void shouldRemoveFirst() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.removeFirst();

        assertEquals("B", list.getFirst());
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Should remove last element")
    void testRemoveLast() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.removeLast();

        assertEquals("A", list.getLast());
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Should remove element by index")
    void shouldRemoveByIndex() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.remove(1);

        assertFalse(list.remove(6));
        assertEquals("C", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Should remove first element")
    void shouldRemoveSingleElement() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.removeFirst();

        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Should add first element")
    void shouldAddToEmptyListViaIndex() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.add(0, "A");

        assertEquals("A", list.getFirst());
        assertEquals("A", list.getLast());
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Should add first element")
    void shouldWorkProperlyWithInvalidIndexOperations() {
        CustomLinkedList<String> list = new CustomLinkedList<>();

        assertFalse(list.remove(5));
        assertFalse(list.add(-1, "X"));
        assertNull(list.get(-1));
    }

    @Test
    @DisplayName("Should add null value")
    void shouldAddNullValue() {
        CustomLinkedList<String> list = new CustomLinkedList<>();

        assertTrue(list.addFirst(null));
        assertEquals(1, list.size());
        assertNull(list.getFirst());
        assertNull(list.get(0));
    }

}