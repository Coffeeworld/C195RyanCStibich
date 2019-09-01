/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;
import models.User;
import utils.DBConnection;

/**
 *
 * @author coffeeworld
 */
public class DAOCustomer {
    
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception {
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        //create query to retrieve user information from database
        String query = "select * from customer";
        ResultSet results = statement.executeQuery(query);
        
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList(); 
        while(results.next()) {
            int customerId = results.getInt("customerId");
            String customerName = results.getString("customerName");
            int addressId = results.getInt("addressId");
            boolean active = false;
            if(results.getInt("active") == 1) {
                active = true;
            } else {
                active = false;
            }
            
            Customer currentCustomer = new Customer(customerId, customerName, active, addressId);
            allCustomers.add(currentCustomer);
        }
        DBConnection.closeConnection();
        return allCustomers;
    }
    
    public static int createCustomer(Customer newCustomer) throws SQLException, Exception {
        int customerStatus = 0;
        if(newCustomer.getCustomerStatus()) {
            customerStatus = 1;
        }
        
        String query = "INSERT INTO customer (customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES"
                + " (" + newCustomer.getCustomerId() + ",'" + newCustomer.getCustomerName() +"'," +
                newCustomer.getAddressId() + "," + newCustomer.getCustomerStatus() + ",CURRENT_TIMESTAMP(),'test',"
                + "CURRENT_TIMESTAMP(),'test')";
        System.out.println("Query to create new customer: " + query);
        
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(query);
        DBConnection.closeConnection();
        if(result == 1) {
            return newCustomer.getCustomerId();
        } else {
            return -1;
        }
    }
    
    public static boolean updateCustomer(Customer customer) throws SQLException, Exception {
        String updateQuery = "UPDATE customer SET customerName = '" + customer.getCustomerName()
                + "', addressId = " + customer.getAddressId() + ", active = " + customer.getCustomerStatus()
                + " WHERE customerId = " + customer.getCustomerId();
        System.out.println("Customer update query: " + updateQuery);
        
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(updateQuery);
        DBConnection.closeConnection();
        if(result == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static int getNextCustomerId() throws SQLException, Exception {
        String query = "select max(customerId) from customer";
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        results.next();
        int nextCustomerId = results.getInt("max(customerId)");
        nextCustomerId++;
        DBConnection.closeConnection();
        return nextCustomerId;
    }
    
    public static boolean deleteCustomer(Customer customer) throws SQLException, Exception {
        String query = "delete from customer where customerId = " + customer.getCustomerId();
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(query);
        DBConnection.closeConnection();
        if(result == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static Customer getCustomer(int customerId) {
        try {
            String query = "select * from customer where customerId = " + customerId;
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet result = statement.executeQuery(query);
            result.next();
            boolean customerStatus;
            if(result.getInt("active") == 1) {
                customerStatus = true;
            } else {
                customerStatus = false;
            }
            Customer customer = new Customer(result.getInt("customerId"),
                    result.getString("customerName"), customerStatus,
                    result.getInt("addressId"));
            DBConnection.closeConnection();
            return customer;

        } catch (SQLException ex) {
            System.out.println("Error retrieving customer information: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error retrieving customer information: " + ex.getMessage());
        }
        return null;
    }
}
