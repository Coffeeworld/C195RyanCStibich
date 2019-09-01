/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static DAO.DAOAppointment.createAppointment;
import static DAO.DAOAppointment.getNextAptId;
import static DAO.DAOAppointment.isAptOverlap;
import static DAO.DAOCustomer.getAllCustomers;
import static DAO.DAOUser.getAllUsers;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;
import models.User;
import static utils.DateTimeConverter.convertLocaltoUTC;

/**
 * FXML Controller class
 *
 * @author coffeeworld
 */
public class AppointmentAddController implements Initializable {

    @FXML
    private TextField aptID;
    @FXML
    private ChoiceBox<Integer> customerID;
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private TextField location;
    @FXML
    private TextField contact;
    @FXML
    private TextField type;
    @FXML
    private TextField url;
    @FXML
    private DatePicker start;
    @FXML
    private DatePicker end;
    @FXML
    private Button backButton;
    @FXML
    private Button submitButton;
    @FXML
    private ChoiceBox<Integer> userID;
    @FXML
    private ComboBox<String> startHour;
    @FXML
    private ComboBox<String> startMin;
    @FXML
    private ComboBox<String> endHour;
    @FXML
    private ComboBox<String> endMin;
    
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    ObservableList<Integer> userIds = FXCollections.observableArrayList();
    ObservableList<Integer> customerIds = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aptID.setEditable(false);
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        startHour.setItems(hours);
        endHour.setItems(hours);
        startMin.setItems(minutes);
        endMin.setItems(minutes);
        try {
            aptID.setPromptText(Integer.toString(getNextAptId()));
        } catch (Exception ex) {
            Logger.getLogger(AppointmentAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            ObservableList<Customer> allCustomers = getAllCustomers();
            for(Customer c : allCustomers) {
                customerIds.add(c.getCustomerId());
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving all customers while loading add appointment form: " + ex.getMessage());
        }
        
        try {
            ObservableList<User> allUsers = getAllUsers();
            for(User u : allUsers) {
                userIds.add(u.getUserID());
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving all users while loading add appointment form: " + ex.getMessage());
        }
        
        userID.setItems(userIds);
        customerID.setItems(customerIds);
    }    

    @FXML
    private void backButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("AppointmentMain.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void submitButtonPushed(ActionEvent event) {
        if(
        customerID.getSelectionModel().isEmpty() ||
        title.getText().trim().isEmpty() ||
        description.getText().trim().isEmpty() ||
        location.getText().trim().isEmpty() ||
        contact.getText().trim().isEmpty() ||
        type.getText().trim().isEmpty() ||
        url.getText().trim().isEmpty() ||
        start.getValue() == null ||
        end.getValue() == null ||
        userID.getSelectionModel().isEmpty() ||
        startHour.getSelectionModel().isEmpty() ||
        startMin.getSelectionModel().isEmpty() ||
        endHour.getSelectionModel().isEmpty() ||
        endMin.getSelectionModel().isEmpty()
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer ID, Title, Description, Location, Contact, Type, URL, Start, End, User ID, and Time Fields are all required.");
            //basic lambda for alert
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            })); 
        } else if(isStartTimeBeforeEndTime()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The start date/time must be before the end date/time");
            //basic lambda for alert
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            })); 
        } else if (isOutsideBusinessHours()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This appointment is outside scheduled business hours of 08:00-17:00 UTC.");
            //basic lambda for alert
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            })); 
        } else if(checkAptOverlap()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This appointment overlaps with another scheduled appointment.");
            //basic lambda for alert
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            })); 
        } else {
            try {
                LocalDate startDate = start.getValue();
                String selectedStartHour = startHour.getValue();
                String selectedStartMin = startMin.getValue();
                String startLDT = startDate + " " + selectedStartHour + ":" + selectedStartMin + ":00";

                LocalDate endDate = end.getValue();
                String selectedEndHour = endHour.getValue();
                String selectedEndMin = endMin.getValue();
                String endLDT = endDate + " " + selectedEndHour + ":" + selectedEndMin + ":00";

                int appointmentId = Integer.parseInt(aptID.getPromptText());
                int customerId = customerID.getValue();
                int userId = userID.getValue();
                String selectedTitle = title.getText();
                String selectedDescription = description.getText();
                String selectedLocation = location.getText();
                String selectedContact = contact.getText();
                String selectedType = type.getText();
                String selectedUrl = url.getText();


                Appointment newAppointment = new Appointment(appointmentId, customerId, userId, selectedTitle,
                        selectedDescription, selectedLocation, selectedContact, selectedType,
                        selectedUrl, startLDT, endLDT);

                if(createAppointment(newAppointment) > 0) {
                    System.out.println("Appointment successfully created");
                    Parent tableViewParent = FXMLLoader.load(getClass().getResource("AppointmentMain.fxml"));
                    Scene tableViewScene = new Scene(tableViewParent);
                    Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    window.setScene(tableViewScene);
                    window.show();
                } else {
                    System.out.println("Appointment creation failed");
                };
            } catch (Exception ex) {
                System.out.println("Error creating new appointment: " + ex.getMessage());
            }
        }
    }
    
    public boolean checkAptOverlap() {
        LocalDate startDate = start.getValue();
        String selectedStartHour = startHour.getValue();
        String selectedStartMin = startMin.getValue();
        String startLDT = startDate + " " + selectedStartHour + ":" + selectedStartMin + ":00";

        LocalDate endDate = end.getValue();
        String selectedEndHour = endHour.getValue();
        String selectedEndMin = endMin.getValue();
        String endLDT = endDate + " " + selectedEndHour + ":" + selectedEndMin + ":00";

        int appointmentId = Integer.parseInt(aptID.getPromptText());
        int customerId = customerID.getValue();
        int userId = userID.getValue();
        String selectedTitle = title.getText();
        String selectedDescription = description.getText();
        String selectedLocation = location.getText();
        String selectedContact = contact.getText();
        String selectedType = type.getText();
        String selectedUrl = url.getText();


        Appointment newAppointment = new Appointment(appointmentId, customerId, userId, selectedTitle,
                selectedDescription, selectedLocation, selectedContact, selectedType,
                selectedUrl, startLDT, endLDT);
        
        return isAptOverlap(newAppointment);
    }
    
    public boolean isOutsideBusinessHours() {
        LocalDate startDate = start.getValue();
        String selectedStartHour = startHour.getValue();
        String selectedStartMin = startMin.getValue();
        String startLDT = startDate + " " + selectedStartHour + ":" + selectedStartMin + ":00";

        LocalDate endDate = end.getValue();
        String selectedEndHour = endHour.getValue();
        String selectedEndMin = endMin.getValue();
        String endLDT = endDate + " " + selectedEndHour + ":" + selectedEndMin + ":00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateStart = LocalDateTime.parse(startLDT, formatter);
        localDateStart = convertLocaltoUTC(localDateStart);
        LocalTime startTime = localDateStart.toLocalTime();
        LocalDateTime localDateEnd = LocalDateTime.parse(endLDT, formatter);
        localDateEnd = convertLocaltoUTC(localDateEnd);
        LocalTime endTime = localDateEnd.toLocalTime();
        
        LocalTime businessStart = LocalTime.of(8,0);
        LocalTime businessEnd = LocalTime.of(17,0);
        
        if(startTime.isBefore(businessStart)|| endTime.isAfter(businessEnd) || endTime.isBefore(businessStart) || startTime.isAfter(businessEnd)) {
            return true;
        }
        
        return false;
    }
    
    public boolean isStartTimeBeforeEndTime() {
        LocalDate startDate = start.getValue();
        String selectedStartHour = startHour.getValue();
        String selectedStartMin = startMin.getValue();
        String startLDT = startDate + " " + selectedStartHour + ":" + selectedStartMin + ":00";

        LocalDate endDate = end.getValue();
        String selectedEndHour = endHour.getValue();
        String selectedEndMin = endMin.getValue();
        String endLDT = endDate + " " + selectedEndHour + ":" + selectedEndMin + ":00";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateStart = LocalDateTime.parse(startLDT, formatter);
        LocalDateTime localDateEnd = LocalDateTime.parse(endLDT, formatter);
        
        if(localDateStart.isAfter(localDateEnd)) {
            return true;
        } else {
            return false;
        }
    }
}
