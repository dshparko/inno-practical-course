package com.innowise.sales.customer.analysis.exception;

public class NoItemsFoundException extends RuntimeException {

    public NoItemsFoundException() {
        super("No products found.");
    }
}
