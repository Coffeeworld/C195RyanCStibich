/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static DAO.DAOAppointment.getAptAptInRange;
import static DAO.DAOAppointment.getCustAptAptInRange;
import DAO.DAOCustomer;
import DAO.DAOReports;
import DAO.DAOUser;
import java.io.IOException;
import java.net.URL;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;
import models.User;

/**
 * FXML Controller class
 *
 * @author coffeeworld
 */
public class ReportsController implements Initializable {

    @FXML
    private TableView<DAOReports.AppointmentTypeReport> appointmentTypeTable;
    @FXML
    private ChoiceBox<Integer> consultantChoiceBox;
    @FXML
    private Button consultantSubmitButton;
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
    private ChoiceBox<Integer> customerChoiceBox;
    @FXML
    private Button customerSubmitButton;
    @FXML
    private Button backButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableColumn<DAOReports.AppointmentTypeReport, Integer> year;
    @FXML
    private TableColumn<DAOReports.AppointmentTypeReport, Integer> month;
    @FXML
    private TableColumn<DAOReports.AppointmentTypeReport, String> type;
    @FXML
    private TableColumn<DAOReports.AppointmentTypeReport, Integer> count;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        count.setCellValueFactory(new PropertyValueFactory<>("typeCount"));  
        appointmentTypeTable.setItems(DAOReports.getAppointmentTypeCount());
        
        ObservableList<Integer> allUserIds = FXCollections.observableArrayList();
        try {
            for(User u : DAOUser.getAllUsers()) {
                int currentUserId = u.getUserID();
                allUserIds.add(currentUserId);
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving all user IDs: " + ex.getMessage());
        }
        consultantChoiceBox.setItems(allUserIds);
        
        ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();
        try {
            for(Customer c : DAOCustomer.getAllCustomers()) {
                int currentCustomerId = c.getCustomerId();
                allCustomerIds.add(currentCustomerId);
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving all customer IDs: " + ex.getMessage());
        }
        customerChoiceBox.setItems(allCustomerIds);
        
        aptCustomerID.setCellValueFactory(new PropertyValueFactory<>("custID"));
        aptUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        aptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        aptDesc.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        aptLoc.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
        aptContact.setCellValueFactory(new PropertyValueFactory<>("aptContact"));
        aptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        aptStart.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
        aptEnd.setCellValueFactory(new PropertyValueFactory<>("aptEnd")); 
        
        aptCustomerID1.setCellValueFactory(new PropertyValueFactory<>("custID"));
        aptUserID1.setCellValueFactory(new PropertyValueFactory<>("userID"));
        aptTitle1.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        aptDesc1.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        aptLoc1.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
        aptContact1.setCellValueFactory(new PropertyValueFactory<>("aptContact"));
        aptType1.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        aptStart1.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
        aptEnd1.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));  
    }    

    @FXML
    private void consultantSubmitButtonPushed(ActionEvent event) throws Exception {
        Date startDate = valueOf(LocalDate.now());
        Date endDate = valueOf(LocalDate.now().plusWeeks(52));
        int consultantId = consultantChoiceBox.getValue();
        
        aptWeekTable.setItems(getAptAptInRange(startDate, endDate, consultantId));
    }

    @FXML
    private void customerSubmitButtonPushed(ActionEvent event) throws Exception {
        Date startDate = valueOf(LocalDate.now());
        Date endDate = valueOf(LocalDate.now().plusWeeks(52));
        int customerId = customerChoiceBox.getValue();
        
        aptWeekTable1.setItems(getCustAptAptInRange(startDate, endDate, customerId));
    }

    @FXML
    private void backButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    
}
