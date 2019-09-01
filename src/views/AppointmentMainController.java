/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static DAO.DAOAddress.getAddress;
import DAO.DAOAppointment;
import static DAO.DAOAppointment.deleteAppointment;
import static DAO.DAOCustomer.getCustomer;
import java.io.IOException;
import java.net.URL;
import static java.sql.Date.valueOf;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;

/**
 * FXML Controller class
 *
 * @author coffeeworld
 */
public class AppointmentMainController implements Initializable {

    @FXML
    private TableView<Appointment> aptWeekTable;
    @FXML
    private TableColumn<Appointment, Integer> aptCustomerID;
    @FXML
    private TableColumn<Appointment, Integer> aptUserID;
    @FXML
    private TableColumn<Appointment, String> aptTitle;
    @FXML
    private TableColumn<Appointment, String> aptDesc;
    @FXML
    private TableColumn<Appointment, String> aptLoc;
    @FXML
    private TableColumn<Appointment, String> aptContact;
    @FXML
    private TableColumn<Appointment, String> aptType;
    @FXML
    private TableColumn<Appointment, String> aptStart;
    @FXML
    private TableColumn<Appointment, String> aptEnd;
    @FXML
    private Label weekCustomerName;
    @FXML
    private Label weekCustomerPhone;
    @FXML
    private TableView<Appointment> aptWeekTable1;
    @FXML
    private TableColumn<Appointment, Integer> aptCustomerID1;
    @FXML
    private TableColumn<Appointment, Integer> aptUserID1;
    @FXML
    private TableColumn<Appointment, String> aptTitle1;
    @FXML
    private TableColumn<Appointment, String> aptDesc1;
    @FXML
    private TableColumn<Appointment, String> aptLoc1;
    @FXML
    private TableColumn<Appointment, String> aptContact1;
    @FXML
    private TableColumn<Appointment, String> aptType1;
    @FXML
    private TableColumn<Appointment, String> aptStart1;
    @FXML
    private TableColumn<Appointment, String> aptEnd1;
    @FXML
    private Label monthCustomerName;
    @FXML
    private Label monthCustomerPhone;
    @FXML
    private Button backButton;
    @FXML
    private Button addAptButton;
    @FXML
    private Button modAptButton;
    @FXML
    private Button delAptButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button weekCustomerButton;
    @FXML
    private Button monthCustomerButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            aptCustomerID.setCellValueFactory(new PropertyValueFactory<>("custID"));
            aptUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            aptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
            aptDesc.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
            aptLoc.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
            aptContact.setCellValueFactory(new PropertyValueFactory<>("aptContact"));
            aptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
            aptStart.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
            aptEnd.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));
            
            Date startDate = valueOf(LocalDate.now());
            Date endDate = valueOf(LocalDate.now().plusWeeks(1));
            aptWeekTable.setItems(DAOAppointment.getAptAptInRange(startDate, endDate));
            
            aptCustomerID1.setCellValueFactory(new PropertyValueFactory<>("custID"));
            aptUserID1.setCellValueFactory(new PropertyValueFactory<>("userID"));
            aptTitle1.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
            aptDesc1.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
            aptLoc1.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
            aptContact1.setCellValueFactory(new PropertyValueFactory<>("aptContact"));
            aptType1.setCellValueFactory(new PropertyValueFactory<>("aptType"));
            aptStart1.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
            aptEnd1.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));
            
            endDate = valueOf(LocalDate.now().plusMonths(1));
            aptWeekTable1.setItems(DAOAppointment.getAptAptInRange(startDate, endDate));
        } catch (Exception ex) {
            System.out.println("Error seeing values for appointment tables: " + ex.getMessage());
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
    private void addAptButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("AppointmentAdd.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void modAptButtonPushed(ActionEvent event) throws IOException, ParseException {
        int tabPaneSelected = tabPane.getSelectionModel().getSelectedIndex();
        Appointment selectedAppointment = new Appointment();
        if(tabPaneSelected == 0) {
            selectedAppointment = aptWeekTable.getSelectionModel().getSelectedItem();
        } else {
            selectedAppointment = aptWeekTable1.getSelectionModel().getSelectedItem();
        }
        
        Stage updateAppointmentStage;
        Parent updateAppointmentParent;
        updateAppointmentStage = (Stage) modAptButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentUpdate.fxml"));
        updateAppointmentParent = loader.load();
        Scene scene = new Scene(updateAppointmentParent);
        updateAppointmentStage.setScene(scene);
        updateAppointmentStage.show();
        AppointmentUpdateController controller = loader.getController();
        controller.setAppointment(selectedAppointment);
    }

    @FXML
    private void delAptButtonPushed(ActionEvent event) throws Exception {
        int tabPaneSelected = tabPane.getSelectionModel().getSelectedIndex();
        Appointment selectedAppointment = new Appointment();
        if(tabPaneSelected == 0) {
            selectedAppointment = aptWeekTable.getSelectionModel().getSelectedItem();
        } else {
            selectedAppointment = aptWeekTable1.getSelectionModel().getSelectedItem();
        }
        
        if(deleteAppointment(selectedAppointment)) {
            System.out.println("Selected appointment " + selectedAppointment.getAptID() + " successfully deleted");
        } else {
            System.out.println("Failure deleting appointment");
        };
    }

    @FXML
    private void weekCustomerButtonPushed(ActionEvent event) throws Exception {
        int selectedCustomerId = aptWeekTable.getSelectionModel().getSelectedItem().getCustID();
        Customer customer = getCustomer(selectedCustomerId);
        weekCustomerName.setText(customer.getCustomerName());
        weekCustomerPhone.setText(getAddress(customer).getPhone());
    }

    @FXML
    private void monthCustomerButtonPushed(ActionEvent event) throws Exception {
        int selectedCustomerId = aptWeekTable1.getSelectionModel().getSelectedItem().getCustID();
        Customer customer = getCustomer(selectedCustomerId);
        monthCustomerName.setText(customer.getCustomerName());
        monthCustomerPhone.setText(getAddress(customer).getPhone());
    }
    
}
