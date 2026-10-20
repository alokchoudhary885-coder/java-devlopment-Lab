package com.shopsphere.swing;

import com.shopsphere.model.User;
import com.shopsphere.swing.view.AdminMainFrame;
import com.shopsphere.swing.view.LoginDialog;

import javax.swing.*;

/**
 * Main Entry Point for the ShopSphere Java Swing Desktop Administration Application.
 * <p>
 * Academic Purpose: Demonstrates Java Swing standalone GUI desktop development,
 * native Look & Feel configuration, dialog modality, and desktop MVC architecture
 * as part of the RTU Advanced Java Coursework.
 * </p>
 */
public class ShopSphereAdminApp {

    public static void main(String[] args) {
        // Set Native Operating System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[ShopSphereAdminApp] Could not set system look and feel: " + e.getMessage());
        }

        // Launch GUI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            if (loginDialog.isSucceeded()) {
                User admin = loginDialog.getAuthenticatedUser();
                System.out.println("[ShopSphereAdminApp] Administrator authenticated: " + admin.getEmail());

                AdminMainFrame mainFrame = new AdminMainFrame(admin);
                mainFrame.setVisible(true);
            } else {
                System.out.println("[ShopSphereAdminApp] Authentication cancelled. Exiting desktop application.");
                System.exit(0);
            }
        });
    }
}
