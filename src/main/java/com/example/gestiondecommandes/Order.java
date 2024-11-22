// Order.java
package com.example.gestiondecommandes;

import java.io.Serializable;

public class Order implements Serializable {
    private String userName;
    private String productName;
    private int quantity;
    private String status;

    public Order(String userName, String productName, int quantity) {
        this.userName = userName;
        this.productName = productName;
        this.quantity = quantity;
        this.status = "Pending";
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}