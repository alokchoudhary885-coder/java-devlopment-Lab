package com.shopsphere.rmi;

import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote Interface for Distributed Inventory Management via Java RMI.
 * <p>
 * Academic Purpose: Fulfills RTU Advanced Java Syllabus (Unit: Java RMI Distributed Computing).
 * Defines remote methods invoked across distinct JVM instances via stubs/skeletons.
 * Every method declares {@link RemoteException}.
 * </p>
 */
public interface RemoteInventoryService extends Remote {

    /**
     * Retrieves product details by ID across the network.
     *
     * @param productId product identifier
     * @return serialized {@link Product} entity
     * @throws RemoteException on network/RMI communication failure
     */
    Product getProduct(int productId) throws RemoteException;

    /**
     * Queries current available stock quantity.
     *
     * @param productId product identifier
     * @return current stock count
     * @throws RemoteException on network/RMI communication failure
     */
    int getStock(int productId) throws RemoteException;

    /**
     * Remotely updates product stock quantity.
     *
     * @param productId   product identifier
     * @param newQuantity new stock count
     * @return true if updated, false otherwise
     * @throws RemoteException on network/RMI communication failure
     */
    boolean updateStock(int productId, int newQuantity) throws RemoteException;

    /**
     * Retrieves featured catalog products.
     *
     * @param limit maximum products to return
     * @return list of serialized products
     * @throws RemoteException on network/RMI communication failure
     */
    List<Product> getFeaturedProducts(int limit) throws RemoteException;

    /**
     * Searches products across remote catalog.
     *
     * @param keyword search term
     * @return list of matching serialized products
     * @throws RemoteException on network/RMI communication failure
     */
    List<Product> searchProducts(String keyword) throws RemoteException;

    /**
     * Calculates total inventory monetary valuation (sum of price * stock).
     *
     * @return inventory valuation as {@link BigDecimal}
     * @throws RemoteException on network/RMI communication failure
     */
    BigDecimal getInventoryValuation() throws RemoteException;
}
