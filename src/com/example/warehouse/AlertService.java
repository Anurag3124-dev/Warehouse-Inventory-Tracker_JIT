package com.example.warehouse;

public interface AlertService {
    void onLowStock(String productId, String productName, int currentQuantity, int threshold);
}
