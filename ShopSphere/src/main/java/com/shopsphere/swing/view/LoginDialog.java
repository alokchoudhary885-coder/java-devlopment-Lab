package com.shopsphere.swing.view;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Authentication Dialog for the ShopSphere Swing Desktop Administration System.
 * Validates administrative credentials against the UserService / database.
 */
public class LoginDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final transient UserService userService;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private User authenticatedUser;
    private boolean succeeded = false;

    public LoginDialog(Frame parent) {
        super(parent, "ShopSphere — Admin Authentication", true);
        this.userService = new UserService();

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(420, 320);
        setLocationRelativeTo(getParent());

        // Header Banner
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(22, 27, 46)); // Admin navy #161b2e
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel("ShopSphere Desktop Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Enterprise Management Portal (RTU Advanced Java)");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitleLabel.setForeground(new Color(142, 149, 173));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 24, 10, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Email Label & Field
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel emailLbl = new JLabel("Admin Email:");
        emailLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        emailField = new JTextField("admin@shopsphere.com", 20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(emailField, gbc);

        // Password Label & Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        passwordField = new JPasswordField("admin123", 20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(passwordField, gbc);

        // Status Feedback Label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(220, 38, 38));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(statusLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Bottom Actions Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginBtn.setBackground(new Color(0, 210, 122)); // Green #00d27a
        loginBtn.setForeground(Color.BLACK);
        loginBtn.addActionListener(this::handleLogin);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelBtn.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        // Enter key activates login
        getRootPane().setDefaultButton(loginBtn);

        buttonPanel.add(cancelBtn);
        buttonPanel.add(loginBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both email and password.");
            return;
        }

        // Validate credentials against UserService
        User user = userService.login(email, password);

        // Fallback demo credentials for offline demonstration
        if (user == null && "admin@shopsphere.com".equalsIgnoreCase(email) && "admin123".equals(password)) {
            user = new User();
            user.setId(1);
            user.setEmail("admin@shopsphere.com");
            user.setFullName("Alok Choudhary (Admin)");
            user.setRole("ADMIN");
            user.setStatus("ACTIVE");
        }

        if (user != null) {
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                statusLabel.setText("Access denied: Account is not an administrator.");
                return;
            }
            authenticatedUser = user;
            succeeded = true;
            dispose();
        } else {
            statusLabel.setText("Invalid email or password. Please try again.");
            passwordField.setText("");
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
