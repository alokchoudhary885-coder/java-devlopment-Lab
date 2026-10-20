package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Application Lifecycle Event Listener.
 * Implements ServletContextListener to handle application startup and graceful shutdown.
 * Demonstrates: ServletContext attributes, application initialization, and driver cleanup.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String appName = ctx.getInitParameter("appName");
        if (appName == null) {
            appName = "ShopSphere";
        }

        System.out.println("=================================================");
        System.out.println(" [ShopSphere] Application Initialized: " + appName);
        System.out.println(" Target: Apache Tomcat 11 / Jakarta EE 10");
        System.out.println(" Runtime: Java " + System.getProperty("java.version"));
        System.out.println("=================================================");

        // Initialize active sessions counter in ServletContext
        ctx.setAttribute("activeSessionsCount", new AtomicInteger(0));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[ShopSphere] Application Context shutting down...");

        // Deregister JDBC drivers to prevent Tomcat memory leaks
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("[ShopSphere] Deregistered JDBC driver: " + driver);
            } catch (SQLException e) {
                System.err.println("[ShopSphere] Error deregistering driver " + driver + ": " + e.getMessage());
            }
        }

        System.out.println("[ShopSphere] Application Context destroyed successfully.");
    }
}
