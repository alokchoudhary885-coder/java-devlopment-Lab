package com.shopsphere.rmi;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Concrete Remote Object Implementation for {@link RemoteInventoryService}.
 * <p>
 * Academic Purpose: Extends {@link UnicastRemoteObject} to export the remote object
 * on an anonymous port, delegates business logic to {@link ProductService},
 * and serializes return entities back to the remote calling client.
 * </p>
 */
public class RemoteInventoryServiceImpl extends UnicastRemoteObject implements RemoteInventoryService {

    private static final long serialVersionUID = 1L;
    private final transient ProductService productService;

    public RemoteInventoryServiceImpl() throws RemoteException {
        super();
        this.productService = new ProductService();
    }

    public RemoteInventoryServiceImpl(int port) throws RemoteException {
        super(port);
        this.productService = new ProductService();
    }

    @Override
    public Product getProduct(int productId) throws RemoteException {
        System.out.println("[RMI Server] Remote invocation: getProduct(" + productId + ")");
        return productService.getProductById(productId);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        System.out.println("[RMI Server] Remote invocation: getStock(" + productId + ")");
        Product p = productService.getProductById(productId);
        return p != null ? p.getStockQuantity() : 0;
    }

    @Override
    public boolean updateStock(int productId, int newQuantity) throws RemoteException {
        System.out.println("[RMI Server] Remote invocation: updateStock(" + productId + ", " + newQuantity + ")");
        return productService.updateStock(productId, newQuantity);
    }

    @Override
    public List<Product> getFeaturedProducts(int limit) throws RemoteException {
        System.out.println("[RMI Server] Remote invocation: getFeaturedProducts(" + limit + ")");
        return productService.getFeaturedProducts(limit);
    }

    @Override
    public List<Product> searchProducts(String keyword) throws RemoteException {
        System.out.println("[RMI Server] Remote invocation: searchProducts('" + keyword + "')");
        return productService.searchProducts(keyword);
    }

    @Override
    public BigDecimal getInventoryValuation() throws RemoteException {
        System.out.println("[RMI Server] Remote invocation: getInventoryValuation()");
        List<Product> products = productService.getAllProducts();
        BigDecimal total = BigDecimal.ZERO;
        for (Product p : products) {
            if (p.getPrice() != null && p.getStockQuantity() > 0) {
                BigDecimal itemVal = p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity()));
                total = total.add(itemVal);
            }
        }
        return total;
    }
}
