package com.example.warehouse;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Warehouse {
	private final Map<String, Product> products = new HashMap<>();
	private final List<AlertService> observers = new ArrayList<>();
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

	public void addObserver(AlertService service) {
		if (service == null)
			return;
		rwLock.writeLock().lock();
		try {
			observers.add(service);
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	private void notifyLowStock(Product p) {
		rwLock.readLock().lock();
		try {
			for (AlertService s : observers) {
				new Thread(() -> s.onLowStock(p.getId(), p.getName(), p.getQuantity(), p.getReorderThreshold())).start();
			}
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void addProduct(Product p) {
		rwLock.writeLock().lock();
		try {
			if (products.containsKey(p.getId()))
				throw new IllegalArgumentException("Product ID already exists");
			products.put(p.getId(), p);
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	public void receiveShipment(String productId, int amount) {
		Product p = getProductById(productId);
		synchronized (p) {
			p.increaseQuantity(amount);
		}
	}

	public void fulfillOrder(String productId, int amount) {
		Product p = getProductById(productId);
		synchronized (p) {
			p.decreaseQuantity(amount);
			if (p.getQuantity() < p.getReorderThreshold())
				notifyLowStock(p);
		}
	}

	public Product getProduct(String id) {
		rwLock.readLock().lock();
		try {
			return products.get(id);
		} finally {
			rwLock.readLock().unlock();
		}
	}

	private Product getProductById(String id) {
		rwLock.readLock().lock();
		try {
			Product p = products.get(id);
			if (p == null)
				throw new NoSuchElementException("No product with ID: " + id);
			return p;
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void saveTo(Path file) throws Exception {
		rwLock.readLock().lock();
		try {
			InventoryPersistence.saveToFile(products, file);
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void loadFrom(Path file) throws Exception {
		rwLock.writeLock().lock();
		try {
			Map<String, Product> loaded = InventoryPersistence.loadFromFile(file);
			products.clear();
			products.putAll(loaded);
		} finally {
			rwLock.writeLock().unlock();
		}
	}
}
