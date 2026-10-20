package com.shopsphere.rmi;

import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Client Runner that connects to the remote Java RMI Server and executes remote method invocations.
 * <p>
 * Academic Purpose: Demonstrates RMI client lookup, remote stub invocation,
 * parameter serialization, and remote exception handling across JVM boundaries.
 * </p>
 */
public class RMIClientRunner {

    public static void main(String[] args) {
        String serviceUrl = RMIServerRunner.RMI_URL;
        System.out.println("=================================================");
        System.out.println(" ShopSphere Java RMI Distributed Client");
        System.out.println(" Connecting to Remote Service: " + serviceUrl);
        System.out.println("=================================================");

        try {
            // 1. Remote Object Lookup
            RemoteInventoryService inventoryService = (RemoteInventoryService) Naming.lookup(serviceUrl);
            System.out.println("[RMIClient] Successfully retrieved remote stub: " + inventoryService.getClass().getName());

            // 2. Remote Method: Heartbeat / Product Query
            System.out.println("\n--- Invoking Remote getProduct(1) ---");
            Product p = inventoryService.getProduct(1);
            if (p != null) {
                System.out.println("Remote Product Result: #" + p.getId() + " - " + p.getName() + " | Price: $" + p.getPrice() + " | Stock: " + p.getStockQuantity());
            } else {
                System.out.println("Remote Product #1 not found.");
            }

            // 3. Remote Method: Stock Query
            System.out.println("\n--- Invoking Remote getStock(1) ---");
            int stock = inventoryService.getStock(1);
            System.out.println("Remote Available Stock for Product #1: " + stock + " units");

            // 4. Remote Method: Catalog Valuation
            System.out.println("\n--- Invoking Remote getInventoryValuation() ---");
            BigDecimal valuation = inventoryService.getInventoryValuation();
            System.out.println("Remote Total Catalog Inventory Valuation: $" + valuation);

            // 5. Remote Method: Search Products
            System.out.println("\n--- Invoking Remote searchProducts('Apple') ---");
            List<Product> searchResults = inventoryService.searchProducts("Apple");
            System.out.println("Found " + searchResults.size() + " remote matches:");
            for (Product item : searchResults) {
                System.out.println("  * #" + item.getId() + " " + item.getName() + " ($" + item.getPrice() + ")");
            }

            System.out.println("\n[RMIClient] All distributed method invocations completed successfully.");

        } catch (NotBoundException e) {
            System.err.println("[RMIClient] Service not bound: " + e.getMessage() + ". (Ensure RMIServerRunner is running)");
        } catch (MalformedURLException e) {
            System.err.println("[RMIClient] Invalid URL: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("[RMIClient] Remote communication exception: " + e.getMessage());
        }
    }
}
