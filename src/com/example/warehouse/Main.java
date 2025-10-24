package com.example.warehouse;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        Warehouse warehouse = new Warehouse();
        warehouse.addObserver(new ConsoleAlertService());

        // Add Laptop
        Product laptop = new Product("P100", "Laptop", 0, 5);
        warehouse.addProduct(laptop);

        // Add TV
        Product tv = new Product("P101", "TV", 0, 5);
        warehouse.addProduct(tv);

        System.out.println("Receiving 10 laptops...");
        warehouse.receiveShipment("P100", 10);
        System.out.println(warehouse.getProduct("P100"));

        System.out.println("Receiving 10 TVs...");
        warehouse.receiveShipment("P101", 10);
        System.out.println(warehouse.getProduct("P101"));

        System.out.println("Fulfilling 6 laptop orders...");
        warehouse.fulfillOrder("P100", 6);
        System.out.println(warehouse.getProduct("P100"));

        System.out.println("Fulfilling 7 TV orders...");
        warehouse.fulfillOrder("P101", 7);
        System.out.println(warehouse.getProduct("P101"));

        Thread.sleep(300);

        Path file = Paths.get("inventory.csv");
        warehouse.saveTo(file);
        System.out.println("Inventory saved to " + file.toAbsolutePath());
    }
}
