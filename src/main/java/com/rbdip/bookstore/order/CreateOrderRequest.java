package com.rbdip.bookstore.order;

import java.util.List;

public record CreateOrderRequest(
        String customerFullName,
        String customerAddress,
        String customerPhone,
        String customerType,
        String couponCode,
        List<Item> items) {

    public record Item(Long productId, Integer quantity) {
    }

    void validateCustomerInfo() {
        if (this.customerFullName() == null || this.customerFullName().isBlank()) {
            throw new IllegalArgumentException("customerFullName is required");
        }
        if (this.customerAddress() == null || this.customerAddress().isBlank()) {
            throw new IllegalArgumentException("customerAddress is required");
        }
    }

    void validateProductInfo() {
        if (this.items() == null || this.items().isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
    }




}
