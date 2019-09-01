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
public class Appointment {
    private int aptID;
    private int custID;
    private int userID;
    private String aptTitle;
    private String aptDescription;
    private String aptLocation;
    private String aptContact;
    private String aptType;
    private String url;
    private String aptStart;
    private String aptEnd;
    
    public Appointment() {};

    public Appointment(int aptID, int custID, int userID, String aptTitle, String aptDescription, String aptLocation, String aptContact, String aptType, String url, String aptStart, String aptEnd) {
        this.aptID = aptID;
        this.custID = custID;
        this.userID = userID;
        this.aptTitle = aptTitle;
        this.aptDescription = aptDescription;
        this.aptLocation = aptLocation;
        this.aptContact = aptContact;
        this.aptType = aptType;
        this.url = url;
        this.aptStart = aptStart;
        this.aptEnd = aptEnd;
    }

    public void setAptID(int aptID) {
        this.aptID = aptID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setAptTitle(String aptTitle) {
        this.aptTitle = aptTitle;
    }

    public void setAptDescription(String aptDescription) {
        this.aptDescription = aptDescription;
    }

    public void setAptLocation(String aptLocation) {
        this.aptLocation = aptLocation;
    }

    public void setAptContact(String aptContact) {
        this.aptContact = aptContact;
    }

    public void setAptType(String aptType) {
        this.aptType = aptType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAptStart(String aptStart) {
        this.aptStart = aptStart;
    }

    public void setAptEnd(String aptEnd) {
        this.aptEnd = aptEnd;
    }

    public int getAptID() {
        return aptID;
    }

    public int getCustID() {
        return custID;
    }

    public int getUserID() {
        return userID;
    }

    public String getAptTitle() {
        return aptTitle;
    }

    public String getAptDescription() {
        return aptDescription;
    }

    public String getAptLocation() {
        return aptLocation;
    }

    public String getAptContact() {
        return aptContact;
    }

    public String getAptType() {
        return aptType;
    }

    public String getUrl() {
        return url;
    }

    public String getAptStart() {
        return aptStart;
    }

    public String getAptEnd() {
        return aptEnd;
    }
    
    
}
