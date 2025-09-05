package com.innowise;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
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
