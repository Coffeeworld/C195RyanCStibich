/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;
import utils.DBConnection;

/**
 *
 * @author coffeeworld
 */
public class DAOUser {
    
    public static boolean login(String userName, String password) throws Exception {
        
        try {
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            
            //create query to retrieve user information from database
            String query = "select * from user where username = '" + userName + "' and password = '" + password + "' and active = 1";
            String dateTime = ZonedDateTime.now().toString();
            
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                String loggerEntry = dateTime + ": user " + userName + " has successfully logged in";
                utils.Logger.createLog(loggerEntry);
                DBConnection.closeConnection();
                return true;
            } else {
                String loggerEntry = dateTime + ": user " + userName + " has failed to log in";
                utils.Logger.createLog(loggerEntry);
                DBConnection.closeConnection();
                return false;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
    
    public static ObservableList<User> getAllUsers() throws SQLException, Exception {
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        //create query to retrieve user information from database
        String query = "select * from user";
        ResultSet results = statement.executeQuery(query);
        
        ObservableList<User> allUsers = FXCollections.observableArrayList(); 
        while(results.next()) {
            int userId = results.getInt("userId");
            String userName = results.getString("userName");
            String password = results.getString("password");
            boolean active = false;
            if(results.getInt("active") == 1) {
                active = true;
            } else {
                active = false;
            }
            
            User currentUser = new User(userId, userName, password, active);
            allUsers.add(currentUser);
        }
        DBConnection.closeConnection();
        return allUsers;

    }
}
