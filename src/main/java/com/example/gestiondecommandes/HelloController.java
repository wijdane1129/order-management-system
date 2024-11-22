// HelloController.java
package com.example.gestiondecommandes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    // Product Management
    @FXML private TextField productNameField;
    @FXML private TextField productPriceField;
    @FXML private TextField productStockField;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;

    // User Management
    @FXML private TextField userNameField;
    @FXML private TextField userEmailField;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> userNameColumn;
    @FXML private TableColumn<User, String> userEmailColumn;

    // Order Management
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<Product> productComboBox;
    @FXML private TextField quantityField;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> orderUserColumn;
    @FXML private TableColumn<Order, String> orderProductColumn;
    @FXML private TableColumn<Order, Integer> orderQuantityColumn;
    @FXML private TableColumn<Order, String> orderStatusColumn;

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize Product Table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productTable.setItems(products);

        // Initialize User Table
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setItems(users);

        // Initialize Order Table
        orderUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        orderProductColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orderQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderTable.setItems(orders);

        // Load saved data
        loadProducts();
        loadUsers();
        loadOrders();

        // Setup ComboBoxes
        userComboBox.setItems(users);
        productComboBox.setItems(products);
    }

    // Product Management Methods
    @FXML
    private void addProduct() {
        try {
            String name = productNameField.getText();
            double price = Double.parseDouble(productPriceField.getText());
            int stock = Integer.parseInt(productStockField.getText());

            Product product = new Product(name, price, stock);
            products.add(product);
            saveProducts();
            clearProductFields();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for price or stock!");
        }
    }

    @FXML
    private void deleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            products.remove(selectedProduct);
            saveProducts();
        }
    }

    // User Management Methods
    @FXML
    private void addUser() {
        String name = userNameField.getText();
        String email = userEmailField.getText();

        if (!name.isEmpty() && !email.isEmpty()) {
            User user = new User(name, email);
            users.add(user);
            saveUsers();
            clearUserFields();
        } else {
            showAlert("Error", "Please fill all user fields!");
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            users.remove(selectedUser);
            saveUsers();
        }
    }

    // Order Management Methods
    @FXML
    private void addOrder() {
        User selectedUser = userComboBox.getValue();
        Product selectedProduct = productComboBox.getValue();

        try {
            int quantity = Integer.parseInt(quantityField.getText());

            if (selectedUser != null && selectedProduct != null && quantity > 0) {
                if (quantity <= selectedProduct.getStock()) {
                    Order order = new Order(selectedUser.getName(), selectedProduct.getName(), quantity);
                    orders.add(order);

                    // Update product stock
                    selectedProduct.setStock(selectedProduct.getStock() - quantity);
                    productTable.refresh();

                    saveOrders();
                    saveProducts();
                    clearOrderFields();
                } else {
                    showAlert("Error", "Insufficient stock!");
                }
            } else {
                showAlert("Error", "Please fill all order fields!");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid quantity!");
        }
    }

    // Helper Methods
    private void clearProductFields() {
        productNameField.clear();
        productPriceField.clear();
        productStockField.clear();
    }

    private void clearUserFields() {
        userNameField.clear();
        userEmailField.clear();
    }

    private void clearOrderFields() {
        userComboBox.setValue(null);
        productComboBox.setValue(null);
        quantityField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Data Persistence Methods
    private void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(new ArrayList<>(products));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("products.dat"))) {
            ArrayList<Product> loadedProducts = (ArrayList<Product>) ois.readObject();
            products.setAll(loadedProducts);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(new ArrayList<>(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            ArrayList<User> loadedUsers = (ArrayList<User>) ois.readObject();
            users.setAll(loadedUsers);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("orders.dat"))) {
            oos.writeObject(new ArrayList<>(orders));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("orders.dat"))) {
            ArrayList<Order> loadedOrders = (ArrayList<Order>) ois.readObject();
            orders.setAll(loadedOrders);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}