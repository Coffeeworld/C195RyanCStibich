/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author coffeeworld
 */
public class User {
    private int userID;
    private String userName;
    private String password;
    private boolean active;
    
    public User() {};
    
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(int userID, String userName, String password, boolean active) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.active = active;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }
    
    
}
