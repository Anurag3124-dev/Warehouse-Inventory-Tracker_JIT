package com.example.warehouse;

public class ConsoleAlertService implements AlertService {
    @Override
    public void onLowStock(String productId, String productName, int currentQuantity, int threshold) {
        System.out.println("RESTOCK ALERT: Low stock for " + productName +
                " (ID: " + productId + ") — only " + currentQuantity +
                " left (threshold: " + threshold + ")");
    }
}
