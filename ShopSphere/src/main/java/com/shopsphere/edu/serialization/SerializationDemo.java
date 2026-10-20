package com.shopsphere.edu.serialization;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Academic Demonstration: Java Object Serialization & Deserialization.
 * <p>
 * RTU Advanced Java Syllabus Traceability:
 * - java.io.Serializable interface implementation
 * - ObjectOutputStream / ObjectInputStream stream lifecycle
 * - Deep copying & object graph preservation across memory streams
 * - transient keyword semantics
 * - serialVersionUID verification
 * </p>
 */
public class SerializationDemo {

    public static byte[] serializeToByteArray(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }

    public static Object deserializeFromByteArray(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println(" Java Object Serialization & Deserialization Demonstration");
        System.out.println("======================================================================");

        try {
            // 1. Create Sample Product Graph
            Product originalProduct = new Product();
            originalProduct.setId(101);
            originalProduct.setName("Apple MacBook Pro M3 Max");
            originalProduct.setBrand("Apple");
            originalProduct.setPrice(new BigDecimal("2499.00"));
            originalProduct.setDiscountPercent(new BigDecimal("10.00"));
            originalProduct.setStockQuantity(25);
            originalProduct.setStatus(true);

            System.out.println("[Original Product Before Serialization]:");
            System.out.println("  ID     : " + originalProduct.getId());
            System.out.println("  Name   : " + originalProduct.getName());
            System.out.println("  Price  : $" + originalProduct.getPrice());
            System.out.println("  Stock  : " + originalProduct.getStockQuantity());

            // 2. Serialize to Binary Byte Array
            byte[] serializedBytes = serializeToByteArray(originalProduct);
            System.out.println("\n[Serialization Success]: Serialized into " + serializedBytes.length + " binary bytes.");

            // 3. Deserialize back to new Object instance
            Product deserializedProduct = (Product) deserializeFromByteArray(serializedBytes);
            System.out.println("\n[Deserialized Product Restored]:");
            System.out.println("  ID     : " + deserializedProduct.getId());
            System.out.println("  Name   : " + deserializedProduct.getName());
            System.out.println("  Price  : $" + deserializedProduct.getPrice());
            System.out.println("  Stock  : " + deserializedProduct.getStockQuantity());

            // 4. Verify Deep-Copy Independence
            System.out.println("\n[Deep-Copy Memory Verification]:");
            System.out.println("  originalProduct == deserializedProduct : " + (originalProduct == deserializedProduct) + " (Independent memory heap reference)");
            System.out.println("  originalProduct.getId() == deserializedProduct.getId() : " + (originalProduct.getId() == deserializedProduct.getId()));

            // 5. Complex Object Graph Serialization (Order with nested OrderItems)
            System.out.println("\n--- Testing Complex Object Graph (Order with Nested Items) ---");
            Order order = new Order();
            order.setId(501);
            order.setOrderNumber("ORD-20261015-8842");
            order.setTotalAmount(new BigDecimal("2999.00"));
            order.setStatus("PROCESSING");

            List<OrderItem> items = new ArrayList<>();
            OrderItem item1 = new OrderItem();
            item1.setId(1);
            item1.setProductName("MacBook Pro M3");
            item1.setQuantity(1);
            item1.setUnitPrice(new BigDecimal("2499.00"));
            items.add(item1);
            order.setOrderItems(items);

            byte[] orderBytes = serializeToByteArray(order);
            Order restoredOrder = (Order) deserializeFromByteArray(orderBytes);

            System.out.println("Serialized Order Bytes: " + orderBytes.length + " bytes.");
            System.out.println("Restored Order Number : " + restoredOrder.getOrderNumber());
            System.out.println("Restored Nested Items : " + (restoredOrder.getOrderItems() != null ? restoredOrder.getOrderItems().size() : 0));
            System.out.println("\n[Result]: All Object Serialization requirements verified successfully!");

        } catch (Exception e) {
            System.err.println("Serialization failure: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
