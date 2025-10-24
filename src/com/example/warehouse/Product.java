package com.example.warehouse;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    private final String id;
    private final String name;
    private int quantity;
    private final int reorderThreshold;

    public Product(String id, String name, int quantity, int reorderThreshold) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (quantity < 0) throw new IllegalArgumentException("quantity must be >= 0");
        if (reorderThreshold < 0) throw new IllegalArgumentException("threshold must be >= 0");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public synchronized int getQuantity() { return quantity; }

    synchronized void increaseQuantity(int delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta must be positive");
        this.quantity += delta;
    }

    synchronized void decreaseQuantity(int delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta must be positive");
        if (delta > this.quantity) throw new IllegalArgumentException("insufficient stock");
        this.quantity -= delta;
    }

    public int getReorderThreshold() { return reorderThreshold; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return id.equals(p.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', qty=%d, threshold=%d}",
                id, name, quantity, reorderThreshold);
    }
}
