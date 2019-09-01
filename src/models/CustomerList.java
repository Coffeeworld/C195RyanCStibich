/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

/**
 *
 * @author coffeeworld
 */
public class CustomerList {
        private int customerId;
        private String customerName;
        private int customerStatus;
        public int addressId;
        public String address;
        public String address2;
        public String postalCode;
        public String phone;
        public int cityId;
        private String city;
        private int countryId;
        private String country;

        public CustomerList(int customerId, String customerName, int customerStatus, int addressId, String address, String address2, String postalCode, String phone, int cityId, String city, int countryId, String country) {
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerStatus = customerStatus;
            this.addressId = addressId;
            this.address = address;
            this.address2 = address2;
            this.postalCode = postalCode;
            this.phone = phone;
            this.cityId = cityId;
            this.city = city;
            this.countryId = countryId;
            this.country = country;
        }
        
        public static Customer getCustomer(CustomerList customerList) {
            boolean custStatus = false;
            if(customerList.getCustomerStatus()) {
                custStatus = true;
            }
            
            Customer customer = new Customer(customerList.getCustomerId(), customerList.getCustomerName(), 
                    custStatus, customerList.getAddressId());
            
            return customer;
        }
        
        public static Address getAddress(CustomerList customerList) {
            Address address = new Address(customerList.getAddressId(), 
                    customerList.getAddress(), customerList.getAddress2(), 
                    customerList.getCityId(), customerList.getPostalCode(), customerList.getPhone());
            return address;
        }
        
        public static ObservableList<CustomerList> getAllCustomerListObjects() throws SQLException, Exception {
            String query = "select customer.*, address.*, city.*, country.*\n" +
                    "from customer, address, city, country\n" + 
                    "where customer.addressId = address.addressId\n" +
                    "and address.cityId = city.cityId\n" +
                    "and city.countryId = country.countryId";
            System.out.println("Query to retrieve all customer list objects: " + query);
            ObservableList<CustomerList> allCustomerListObjects = FXCollections.observableArrayList();
            
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                int customerId = results.getInt("customerId");
                String customerName = results.getString("customerName");
                int customerStatus = results.getInt("active");
                int addressId = results.getInt("addressId");
                String address = results.getString("address");
                String address2 = results.getString("address2");
                String postalCode = results.getString("postalCode");
                String phone = results.getString("phone");
                int cityId = results.getInt("cityId");
                String city = results.getString("city");
                int countryId = results.getInt("countryId");
                String country = results.getString("country");
                
                CustomerList currentCustomerList = new CustomerList(customerId, 
                customerName, customerStatus, addressId, address, address2, 
                        postalCode, phone, cityId, city, countryId, country);
                allCustomerListObjects.add(currentCustomerList);
            }
            DBConnection.closeConnection();
            return allCustomerListObjects;
        }
        
        public static ObservableList<String> getAllCityNames() throws SQLException, Exception {
            String query = "select city.*, country.* from city, country where city.countryId = country.countryId";
            ObservableList<String> allCityNames = FXCollections.observableArrayList();
            
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                String city = results.getString("city");
                String country = results.getString("country");
                String cityCountry = city + ", " + country;
                allCityNames.add(cityCountry);
            }
            DBConnection.closeConnection();
            return allCityNames;
        }
        
        public static String getCityCountryName(int cityId, int countryId) throws SQLException, Exception {
            String query = "select city.*, country.* from city, country where city.countryId = country.countryId and "
                    + "cityId = " + cityId + " and country.countryId = " + countryId;
            System.out.println("Query to retrieve city, country: " + query);
            
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            results.next();
            String cityCountry = results.getString("city") + ", " + results.getString("country");
            return cityCountry;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public boolean getCustomerStatus() {
            if(customerStatus == 1) {
                return true;
            } else {
                return false;
            }
        }

        public int getAddressId() {
            return addressId;
        }

        public String getAddress() {
            return address;
        }

        public String getAddress2() {
            return address2;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getPhone() {
            return phone;
        }

        public int getCityId() {
            return cityId;
        }

        public String getCity() {
            return city;
        }

        public int getCountryId() {
            return countryId;
        }

        public String getCountry() {
            return country;
        }
        
        
    }