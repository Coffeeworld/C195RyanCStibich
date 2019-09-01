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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import models.Address;
import models.Customer;
import utils.DBConnection;

/**
 *
 * @author coffeeworld
 */
public class DAOAddress {
    
    public static int createAddress(Address newAddress) throws SQLException, Exception {
        String query = "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (" + newAddress.getAddressId() + ",'"
                + newAddress.getAddress() + "','" + newAddress.getAddress2() + "'," + newAddress.getCityId() + ",'"
                + newAddress.getPostalCode() + "','" + newAddress.getPhone() + "',CURRENT_TIMESTAMP(),'test',"
                + "CURRENT_TIMESTAMP(),'test')";
        System.out.println("Query to insert a new address: " + query);
        
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(query);
        DBConnection.closeConnection();
        if(result == 1) {
            return newAddress.getAddressId();
        } else {
            return -1;
        }
    }
    
    public static boolean deleteAddress(Address address) throws SQLException, Exception {
        String query = "delete from address where addressId = " + address.getAddressId();
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
    
    public static int getNextAddressId() throws Exception {
        String query = "SELECT max(addressId) FROM address";
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        results.next();
        int nextAddressId = results.getInt("max(AddressId)");
        nextAddressId++;
        DBConnection.closeConnection();
        return nextAddressId;
    }
    
    public static int getAddressId(String address, String address2, String postalCode, String phone, int cityId) throws SQLException, Exception {
        String query = "SELECT addressId FROM address WHERE address = '" + address + "' AND "
                + "address2 = '" + address2 + "' AND postalCode = '" + postalCode +
                "' AND phone = '" + phone + "' AND cityId = " + cityId;
        System.out.println("Query to retrieve address ID: " + query);
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        int addressId = -1;
        if(results.next()) {
            addressId = results.getInt("addressId");
            DBConnection.closeConnection();
        } else {
            Address newAddress = new Address(getNextAddressId(), address, address2, 
                    cityId, postalCode, phone);
            addressId = createAddress(newAddress);
            DBConnection.closeConnection();
        }
        return addressId;
    }
    
    public static int getAddressId(Address address) throws SQLException, Exception {
        try {
            //check if address is used by other customers
            String query = "SELECT count(*) FROM customer WHERE addressId = " + address.getAddressId();
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            results.next();
            int customerCount = results.getInt("count(*)");
            
            //if address is used by other customers, prompt user to continue with update/change            
            if(customerCount > 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Other customers are using this address. "
                        + "Click OK to update this address for all customers. "
                        + "Click CANCEL to create a new address for this customer.");
                alert.showAndWait().ifPresent((response -> {
                    //if they want to update, process update
                    if (response == ButtonType.OK) {
                        try {
                            alert.close();
                            String updateQuery = "UPDATE address SET address = '" + address.getAddress() +
                                    "', address2 = '" + address.getAddress2() +
                                    "', cityId = " + address.getCityId() +
                                    ", postalCode = '" + address.getPostalCode() +
                                    "', phone = '" + address.getPhone() +
                                    "', lastUpdate = CURRENT_TIMESTAMP(), lastUpdateBy = 'test'"
                                    + "WHERE addressId = " + address.getAddressId();
                            DBConnection.makeConnection();
                            Statement updateStatement = DBConnection.getConn().createStatement();
                            int updateResults = statement.executeUpdate(updateQuery);
                            DBConnection.closeConnection();
                            //if they don't want to update, create new address    
                        } catch (SQLException ex) {
                            System.out.println("Error updating address for multiple customers: " + ex.getMessage());
                        } catch (Exception ex) {
                            System.out.println("Error updating address for multiple customers: " + ex.getMessage());
                        }
                    } else {
                        alert.close();
                        try {
                            address.setAddressId(getAddressId(address.getAddress(), address.getAddress2(), address.getPostalCode(), address.getPhone(), address.getCityId()));
                        } catch (Exception ex) {
                            System.out.println("Error creating new address: " + ex.getMessage());
                        }
                    }
                }));
            } else {
                //update
                String updateQuery = "UPDATE address SET address = '" + address.getAddress() +
                        "', address2 = '" + address.getAddress2() +
                        "', cityId = " + address.getCityId() +
                        ", postalCode = '" + address.getPostalCode() +
                        "', phone = '" + address.getPhone() +
                        "', lastUpdate = CURRENT_TIMESTAMP(), lastUpdateBy = 'test'"
                        + "WHERE addressId = " + address.getAddressId();
                DBConnection.makeConnection();
                Statement updateStatement = DBConnection.getConn().createStatement();
                int updateResults = statement.executeUpdate(updateQuery);
                DBConnection.closeConnection();
            }
            
        } catch (SQLException ex) {
            System.out.println("Error updating address: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error updating address: " + ex.getMessage());
        }
        DBConnection.closeConnection();
        return address.getAddressId();
    }
    
    public static int getCityId(String city, int countryId) throws SQLException, Exception {
        String query = "select cityId from city where city = '" + city + "' and countryId = " + countryId;
        System.out.println("Query to retrieve city ID " + query);
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        results.next();
        int cityId = results.getInt("cityId");
        DBConnection.closeConnection();
        return countryId;
    }
    
    public static int getCountryId(String country) throws SQLException, Exception {
        String query = "select countryId from country where country = '" + country + "'";
        System.out.println("Query to retrieve country ID: " + query);
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        results.next();
        int countryId = results.getInt("countryId");
        DBConnection.closeConnection();
        return countryId;
    }
    
    public static Address getAddress(Customer customer) throws SQLException, Exception {
        String query = "select * from address where addressId = " + customer.getAddressId();
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        results.next();
        Address address = new Address(results.getInt("addressId"), results.getString("address"), 
                results.getString("address2"), results.getInt("cityId"), 
                results.getString("postalCode"), results.getString("phone"));
        DBConnection.closeConnection();
        return address;
    }
}
