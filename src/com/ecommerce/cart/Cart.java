package com.ecommerce.cart;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private final Map<String, Integer> productIdToQuantity = new LinkedHashMap<>();

    public synchronized void add(String productId, int quantity) {
        if (quantity <= 0) return;
        productIdToQuantity.merge(productId, quantity, Integer::sum);
    }

    public synchronized void remove(String productId) {
        productIdToQuantity.remove(productId);
    }

    public synchronized Map<String, Integer> snapshot() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(productIdToQuantity));
    }

    public synchronized boolean isEmpty() {
        return productIdToQuantity.isEmpty();
    }
}
