package com.example.warehouse;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InventoryPersistence {

    public static void saveToFile(Map<String, Product> products, Path file) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(file)) {
            for (Product p : products.values()) {
                String line = String.format("%s,%s,%d,%d",
                        escape(p.getId()), escape(p.getName()), p.getQuantity(), p.getReorderThreshold());
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public static Map<String, Product> loadFromFile(Path file) throws IOException {
        Map<String, Product> map = new HashMap<>();
        if (!Files.exists(file)) return map;
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                String id = unescape(parts[0]);
                String name = unescape(parts[1]);
                int qty = Integer.parseInt(parts[2]);
                int threshold = Integer.parseInt(parts[3]);
                map.put(id, new Product(id, name, qty, threshold));
            }
        }
        return map;
    }

    private static String escape(String s) { return s.replace(",", "%2C"); }
    private static String unescape(String s) { return s.replace("%2C", ","); }
}
