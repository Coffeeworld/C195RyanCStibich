/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static DAO.DAOAddress.deleteAddress;
import static DAO.DAOCustomer.deleteCustomer;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Address;
import models.Customer;
import models.CustomerList;
import static models.CustomerList.getAddress;
import static models.CustomerList.getCustomer;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author coffeeworld
 */
public class CustomerMainController implements Initializable {

    @FXML
    private TableView<CustomerList> customerTable;
    @FXML
    private TableColumn<CustomerList, Integer> customerId;
    @FXML
    private TableColumn<CustomerList, String> customerName;
    @FXML
    private TableColumn<CustomerList, Integer> customerStatus;
    @FXML
    private TableColumn<CustomerList, String> customerAddress;
    @FXML
    private TableColumn<CustomerList, String> customerAddress2;
    @FXML
    private TableColumn<CustomerList, String> customerPostal;
    @FXML
    private TableColumn<CustomerList, String> customerPhone;
    @FXML
    private TableColumn<CustomerList, String> customerCity;
    @FXML
    private TableColumn<CustomerList, String> customerCountry;
    @FXML
    private Button backButton;
    @FXML
    private Button addCustButton;
    @FXML
    private Button modCustButton;
    @FXML
    private Button delCustButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerStatus.setCellValueFactory(new PropertyValueFactory<>("customerStatus"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        try {
            customerTable.setItems(CustomerList.getAllCustomerListObjects());
        } catch (Exception ex) {
            System.out.println("Error loading CustomerList objects to table: " + ex.getMessage());
        }
    }    

    @FXML
    private void backButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void addCustButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("CustomerAdd.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void modCustButtonPushed(ActionEvent event) throws IOException, Exception {
        CustomerList selectedCustomerList = customerTable.getSelectionModel().getSelectedItem();
        
        Stage updateCustomerListStage;
        Parent updateCustomerListParent;
        updateCustomerListStage = (Stage) modCustButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerUpdate.fxml"));
        updateCustomerListParent = loader.load();
        Scene scene = new Scene(updateCustomerListParent);
        updateCustomerListStage.setScene(scene);
        updateCustomerListStage.show();
        CustomerUpdateController controller = loader.getController();
        controller.setCustomerList(selectedCustomerList);
    }

    @FXML
    private void delCustButtonPushed(ActionEvent event) throws Exception {
        CustomerList customerListToDelete = customerTable.getSelectionModel().getSelectedItem();
        Customer customerToDelete = getCustomer(customerListToDelete);
        if(deleteCustomer(customerToDelete)) {
            System.out.println("Customer successfully deleted");
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("CustomerMain.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        } else {
            System.out.println("Failure deleting customer");
        }
        
        Address addressToDelete = getAddress(customerListToDelete);
        if(deleteAddress(addressToDelete)) {
            System.out.println("Address successfully deleted");
        } else {
            System.out.println("Failure deleting address");
        }
    }
    
    
}


