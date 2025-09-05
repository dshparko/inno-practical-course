package com.innowise.exception;

public class NoItemsFoundException extends RuntimeException {

    public NoItemsFoundException() {
        super("No products found.");
    }
}
