/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195ryancstibich;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 *
 * @author coffeeworld
 */
public class C195RyanCStibich extends Application {
    
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        //Locale.setDefault(Locale.ENGLISH);
        //Locale.setDefault(Locale.GERMAN);
        ResourceBundle rb = ResourceBundle.getBundle("languages/rb");
        
        Parent main = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            loader.setResources(rb);
            main = loader.load();
            
            Scene scene = new Scene(main);
            
            stage.setScene(scene);
            
            stage.show();
        } catch (IOException ex) {
            System.out.println("Error loading Login.fxml at startup");
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Stage getStage() {
        return stage;
    }
}
