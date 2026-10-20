package com.shopsphere.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server Runner that boots the Java RMI Registry and binds the {@link RemoteInventoryService}.
 * <p>
 * Academic Purpose: Demonstrates programmatic RMI registry initialization on standard port 1099,
 * service registration (rebind), and lifecycle management for RTU coursework.
 * </p>
 */
public class RMIServerRunner {

    public static final int RMI_PORT = 1099;
    public static final String SERVICE_NAME = "ShopSphereInventoryService";
    public static final String RMI_URL = "rmi://localhost:" + RMI_PORT + "/" + SERVICE_NAME;

    public static void main(String[] args) {
        try {
            System.out.println("=================================================");
            System.out.println(" ShopSphere Java RMI Distributed Computing Server");
            System.out.println(" RTU Advanced Java RMI Module Initializing...");
            System.out.println("=================================================");

            // 1. Create or Locate RMI Registry on Port 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("[RMIServerRunner] Created local RMI Registry on port " + RMI_PORT);
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(RMI_PORT);
                System.out.println("[RMIServerRunner] Located existing RMI Registry on port " + RMI_PORT);
            }

            // 2. Instantiate Remote Object Implementation
            RemoteInventoryService service = new RemoteInventoryServiceImpl();

            // 3. Bind Remote Object to Registry URL
            Naming.rebind(RMI_URL, service);
            System.out.println("[RMIServerRunner] Service successfully bound to: " + RMI_URL);
            System.out.println("[RMIServerRunner] Remote object ready for client invocations.");
            System.out.println("Press Ctrl+C to stop RMI server.");

        } catch (RemoteException | MalformedURLException e) {
            System.err.println("[RMIServerRunner] Fatal error launching RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
