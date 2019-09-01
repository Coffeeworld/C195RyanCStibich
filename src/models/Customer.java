/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author coffeeworld
 */
public class Customer {
    private int customerId;
    private String customerName;
    private boolean customerStatus;
    private int addressId;
    
    public Customer() {};

    public Customer(int customerId, String customerName, boolean customerStatus, int addressId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerStatus = customerStatus;
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public boolean getCustomerStatus() {
        return customerStatus;
    }

    public int getAddressId() {
        return addressId;
    }
    
    
}
