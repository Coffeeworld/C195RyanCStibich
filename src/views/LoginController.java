/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import DAO.DAOAppointment;
import DAO.DAOUser;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author coffeeworld
 */
public class LoginController implements Initializable {

    @FXML
    private Label header;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    
    boolean userValid = false;
    String submittedUsername;
    String submittedPassword;
    //Locale userLocale = Locale.getDefault();
    //userLocale = Locale.GERMAN;
    //ResourceBundle rB = ResourceBundle.getBundle("languages/rb",userLocale);
    ResourceBundle rb;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Label timeZone;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        System.out.println(Locale.getDefault());

        header.setText(rb.getString("title"));
        userNameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        timeZoneLabel.setText(rb.getString("timezone"));
        timeZone.setText(TimeZone.getDefault().getID());
    }    

    @FXML
    private void loginButtonPushed(ActionEvent event) throws Exception {
        //set username and password to be the ones provided on the form
        submittedUsername = username.getText();
        submittedPassword = password.getText();
        
        if(DAOUser.login(submittedUsername, submittedPassword)) {
            //move on to the next screen
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
            
            System.out.println("Login successful");
            
            if(DAOAppointment.checkAptIn15Min()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("An appointment is scheduled to start within 15 minutes");
                //basic lambda for alert
                alert.showAndWait().ifPresent((response -> { //basic lambda for alert
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));   
            }
        } else {
            //lambda alert with error message for unique code occurence
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(rb.getString("error"));
            
            alert.showAndWait().ifPresent((response -> { //basic lambda for alert
                if (response == ButtonType.OK) {
                    alert.close();
                }
            }));
            System.out.println("Login unsuccessful");
        }
    }

}
