/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static DAO.DAOCustomer.getNextCustomerId;
import static DAO.DAOAddress.*;
import static DAO.DAOCustomer.createCustomer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Customer;
import models.CustomerList;
import static models.CustomerList.getAllCityNames;

/**
 * FXML Controller class
 *
 * @author coffeeworld
 */
public class CustomerAddController implements Initializable {

    @FXML
    private TextField customerId;
    @FXML
    private TextField customerName;
    @FXML
    private TextField address;
    @FXML
    private TextField address2;
    @FXML
    private ChoiceBox<String> city;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField phone;
    @FXML
    private CheckBox active;
    @FXML
    private Button backButton;
    @FXML
    private Button submitButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerId.setEditable(false);
        try {
            ObservableList<String> cityNames = getAllCityNames();
            city.setItems(cityNames); 
        } catch (Exception ex) {
            System.out.println("Error adding city names to city ChoiceBox: " + ex.getMessage());
        }        
        
        try {
            customerId.setPromptText(Integer.toString(getNextCustomerId()));
        } catch (Exception ex) {
            System.out.println("Error getting next customer ID: " + ex.getMessage());
        }
    }    

    @FXML
    private void backButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("CustomerMain.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void submitButtonPushed(ActionEvent event) throws Exception {
        if((customerName.getText().trim().isEmpty()) || (customerName.getText() == null) ||
                (address.getText().trim().isEmpty()) || (address.getText() == null) ||
                (city.getSelectionModel().isEmpty()) ||
                (postalCode.getText().trim().isEmpty()) || (postalCode.getText() == null) ||
                (phone.getText().trim().isEmpty()) || (phone.getText()== null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Name, Address, City, Postal Code, and Phone are required field");
            //basic lambda for alert
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            })); 
        } else {
            int selectedCustomerId = Integer.parseInt(customerId.getPromptText());
            String selectedCustomerName = customerName.getText();
            String selectedAddress = address.getText();
            String selectedAddress2 = address2.getText();
            String selectedCityCountry = city.getValue();
            String selectedPostalCode = postalCode.getText();
            String selectedPhone = phone.getText();
            boolean selectedStatus = active.isSelected();

            String delims = "[,]";
            String[] location = selectedCityCountry.split(delims);
            String selectedCity = location[0];
            String selectedCountry = location[1];
            selectedCountry = selectedCountry.replaceAll("\\s+", "");

            int selectedCountryId = getCountryId(selectedCountry);
            int selectedCityId = getCityId(selectedCity, selectedCountryId);
            //this will create the address if necessary
            int selectedAddressId = getAddressId(selectedAddress, selectedAddress2, selectedPostalCode, selectedPhone, selectedCityId);

            Customer newCustomer = new Customer(selectedCustomerId, selectedCustomerName, selectedStatus, selectedAddressId);

            createCustomer(newCustomer);

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("CustomerMain.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
    }
    
}
