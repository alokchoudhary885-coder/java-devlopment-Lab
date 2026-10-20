package com.shopsphere.swing.view;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Modal Dialog for Adding or Editing a Product in Swing.
 */
public class ProductDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final transient ProductService productService;
    private final Product existingProduct;
    private boolean saved = false;

    private JTextField nameField;
    private JTextField brandField;
    private JComboBox<CategoryItem> categoryCombo;
    private JTextField priceField;
    private JTextField discountField;
    private JSpinner stockSpinner;
    private JCheckBox statusCheck;
    private JTextField imageUrlField;
    private JTextArea descArea;

    public ProductDialog(Frame parent, Product productToEdit) {
        super(parent, productToEdit == null ? "Add New Product" : "Edit Product #" + productToEdit.getId(), true);
        this.productService = new ProductService();
        this.existingProduct = productToEdit;

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(480, 520);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(16, 20, 16, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 5, 5, 5);

        // Name
        g.gridx = 0; g.gridy = 0; g.weightx = 0.3;
        formPanel.add(new JLabel("Product Name:"), g);
        g.gridx = 1; g.weightx = 0.7;
        nameField = new JTextField(existingProduct != null ? existingProduct.getName() : "", 20);
        formPanel.add(nameField, g);

        // Category
        g.gridx = 0; g.gridy = 1;
        formPanel.add(new JLabel("Category:"), g);
        g.gridx = 1;
        categoryCombo = new JComboBox<>();
        List<Category> categories = productService.getAllCategories();
        for (Category c : categories) {
            categoryCombo.addItem(new CategoryItem(c.getId(), c.getName()));
        }
        if (existingProduct != null && existingProduct.getCategoryId() > 0) {
            for (int i = 0; i < categoryCombo.getItemCount(); i++) {
                if (categoryCombo.getItemAt(i).id == existingProduct.getCategoryId()) {
                    categoryCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        formPanel.add(categoryCombo, g);

        // Brand
        g.gridx = 0; g.gridy = 2;
        formPanel.add(new JLabel("Brand:"), g);
        g.gridx = 1;
        brandField = new JTextField(existingProduct != null ? existingProduct.getBrand() : "", 20);
        formPanel.add(brandField, g);

        // Price
        g.gridx = 0; g.gridy = 3;
        formPanel.add(new JLabel("Price ($):"), g);
        g.gridx = 1;
        priceField = new JTextField(existingProduct != null && existingProduct.getPrice() != null ? existingProduct.getPrice().toString() : "99.99", 20);
        formPanel.add(priceField, g);

        // Discount
        g.gridx = 0; g.gridy = 4;
        formPanel.add(new JLabel("Discount (%):"), g);
        g.gridx = 1;
        discountField = new JTextField(existingProduct != null && existingProduct.getDiscountPercent() != null ? existingProduct.getDiscountPercent().toString() : "0.00", 20);
        formPanel.add(discountField, g);

        // Stock
        g.gridx = 0; g.gridy = 5;
        formPanel.add(new JLabel("Stock Quantity:"), g);
        g.gridx = 1;
        stockSpinner = new JSpinner(new SpinnerNumberModel(existingProduct != null ? existingProduct.getStockQuantity() : 50, 0, 10000, 1));
        formPanel.add(stockSpinner, g);

        // Image URL
        g.gridx = 0; g.gridy = 6;
        formPanel.add(new JLabel("Image URL:"), g);
        g.gridx = 1;
        imageUrlField = new JTextField(existingProduct != null ? existingProduct.getImageUrl() : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=100&q=80", 20);
        formPanel.add(imageUrlField, g);

        // Status
        g.gridx = 0; g.gridy = 7;
        formPanel.add(new JLabel("Active / Published:"), g);
        g.gridx = 1;
        statusCheck = new JCheckBox("Published in catalog", existingProduct == null || existingProduct.isStatus());
        formPanel.add(statusCheck, g);

        // Description
        g.gridx = 0; g.gridy = 8;
        formPanel.add(new JLabel("Description:"), g);
        g.gridx = 1;
        descArea = new JTextArea(existingProduct != null ? existingProduct.getDescription() : "", 3, 20);
        descArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descArea), g);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton(existingProduct == null ? "Add Product" : "Save Changes");
        saveBtn.setBackground(new Color(0, 210, 122));
        saveBtn.setForeground(Color.BLACK);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveBtn.addActionListener(e -> handleSave());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal discount;
        try {
            discount = new BigDecimal(discountField.getText().trim());
        } catch (Exception ex) {
            discount = BigDecimal.ZERO;
        }

        Product p = existingProduct != null ? existingProduct : new Product();
        p.setName(name);
        p.setBrand(brandField.getText().trim());

        CategoryItem catItem = (CategoryItem) categoryCombo.getSelectedItem();
        if (catItem != null) {
            p.setCategoryId(catItem.id);
        }

        p.setPrice(price);
        p.setDiscountPercent(discount);
        p.setStockQuantity((Integer) stockSpinner.getValue());
        p.setImageUrl(imageUrlField.getText().trim());
        p.setStatus(statusCheck.isSelected());
        p.setDescription(descArea.getText().trim());

        boolean ok;
        if (existingProduct == null) {
            ok = productService.addProduct(p);
        } else {
            ok = productService.updateProduct(p);
        }

        if (ok) {
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save product to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    private static class CategoryItem {
        final int id;
        final String name;

        CategoryItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
