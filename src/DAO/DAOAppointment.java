/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import utils.DBConnection;
import static utils.DateTimeConverter.convertLocaltoUTC;
import static utils.DateTimeConverter.convertUTCtoLocal;

/**
 *
 * @author coffeeworld
 */
public class DAOAppointment {
    
    public static int createAppointment(Appointment newAppointment) throws SQLException, Exception {
        String ldtStart = newAppointment.getAptStart();
        String ldtEnd = newAppointment.getAptEnd();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateStart = LocalDateTime.parse(ldtStart, formatter);
        localDateStart = convertLocaltoUTC(localDateStart);
        ldtStart = localDateStart.format(formatter);
        System.out.println(newAppointment.getAptStart());
        System.out.println(ldtStart);
        LocalDateTime localDateEnd = LocalDateTime.parse(ldtEnd, formatter);
        localDateEnd = convertLocaltoUTC(localDateEnd);
        ldtEnd = localDateEnd.format(formatter);
        
        String insert = "insert into appointment (appointmentId, customerId, userId, title,"
                + "description, location, contact, type, url, start, end, createDate, createdBy, "
                + "lastUpdate, lastUpdateBy) values ("
                + newAppointment.getAptID() + "," + newAppointment.getCustID() + "," 
                + newAppointment.getUserID() + ",'" + newAppointment.getAptTitle() + "','"
                + newAppointment.getAptDescription() + "','" + newAppointment.getAptLocation() + "','"
                + newAppointment.getAptContact() + "','" + newAppointment.getAptType() + "','"
                + newAppointment.getUrl() + "','" + ldtStart + "','"
                + ldtEnd + "',CURRENT_TIMESTAMP(),'test',CURRENT_TIMESTAMP(),'test')";
        System.out.println("Create appointment query: " + insert);
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(insert);
        DBConnection.closeConnection();
        if(result == 1) {
            return newAppointment.getAptID();
        } else {
            return -1;
        }
    }
    
    public static boolean updateAppointment(Appointment appointment) throws SQLException, Exception {
        String ldtStart = appointment.getAptStart();
        String ldtEnd = appointment.getAptEnd();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateStart = LocalDateTime.parse(ldtStart, formatter);
        localDateStart = convertLocaltoUTC(localDateStart);
        ldtStart = localDateStart.format(formatter);
        System.out.println(localDateStart);
        LocalDateTime localDateEnd = LocalDateTime.parse(ldtEnd, formatter);
        localDateEnd = convertLocaltoUTC(localDateEnd);
        ldtEnd = localDateEnd.format(formatter);
        
        String update = "UPDATE appointment SET "
            + "customerId = " + appointment.getCustID()
            + ", userId = " + appointment.getUserID()
            + ", title = '" + appointment.getAptTitle()
            + "', description = '" + appointment.getAptDescription()
            + "', location = '" + appointment.getAptLocation()
            + "', contact = '" + appointment.getAptContact()
            + "', type = '" + appointment.getAptType()
            + "', url = '" + appointment.getUrl()
            + "', start = '" + ldtStart
            + "', end = '" + ldtEnd
            + "', lastUpdate = CURRENT_TIMESTAMP(), lastUpdateBy = 'test'"
            + "WHERE appointmentId = " + appointment.getAptID();
        System.out.println("Query to update appointment: " + update);
        
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(update);
        if(result == 1) {
            DBConnection.closeConnection();
            return true;
        } else {
            DBConnection.closeConnection();
            return false;
        }
    }
    
    public static boolean deleteAppointment(Appointment appointment) throws Exception {
        String query = "delete from appointment where appointmentId = " + appointment.getAptID();
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        int result = statement.executeUpdate(query);
        DBConnection.closeConnection();
        if(result == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static int getNextAptId() throws Exception {
        String query = "select max(appointmentId) from appointment";
        DBConnection.makeConnection();
        Statement statement = DBConnection.getConn().createStatement();
        ResultSet results = statement.executeQuery(query);
        results.next();
        int nextAptId = results.getInt("max(appointmentId)");
        nextAptId++;
        return nextAptId;
    }
    
    public static ObservableList<Appointment> getAptAptInRange(Date startDate, Date endDate) throws SQLException, Exception {
        ObservableList<Appointment> displayAppointments = FXCollections.observableArrayList();
        String query = "select * from appointment where start >= '" + startDate + "' and end <= '" + endDate + "'";
        System.out.println("Query to retrieve appointments in range: " + query);
        
        try {
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            
            while(results.next()) {
                int appointmentId = results.getInt("appointmentId");
                int customerId = results.getInt("customerId");
                int userId = results.getInt("userId");
                String title = results.getString("title");
                String description = results.getString("description");
                String location = results.getString("location");
                String contact = results.getString("contact");
                String type = results.getString("type");
                String url = results.getString("url");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                Timestamp appointmentStart = results.getTimestamp("start");
                String aptStartDate = appointmentStart.toString();
                LocalDateTime startLDT = LocalDateTime.parse(aptStartDate, formatter);
                startLDT = convertUTCtoLocal(startLDT);
                aptStartDate = startLDT.toString();
                
                Timestamp appointmentEnd = results.getTimestamp("end");
                String aptEndDate = appointmentEnd.toString();
                LocalDateTime endLDT = LocalDateTime.parse(aptEndDate, formatter);
                endLDT = convertUTCtoLocal(endLDT);
                aptEndDate = endLDT.toString();

                
                Appointment currentAppointment = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type,url, aptStartDate, aptEndDate);
                displayAppointments.add(currentAppointment);
            }
            
        }
        catch(Exception e) {
            System.out.println("Error retrieving appointments in range " + startDate + " to " + endDate + ": " + e.getLocalizedMessage());
        }
        DBConnection.closeConnection();        
        return displayAppointments;
    }
    
    public static ObservableList<Appointment> getAptAptInRange(Date startDate, Date endDate, int consultantId) throws Exception {
        ObservableList<Appointment> displayAppointments = FXCollections.observableArrayList();
        String query = "select * from appointment where start >= '" + startDate + "' and end <= '" + endDate + "' and userId = " + consultantId;
        System.out.println("Query to retrieve appointments in range: " + query);
        
        try {
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            
            while(results.next()) {
                int appointmentId = results.getInt("appointmentId");
                int customerId = results.getInt("customerId");
                int userId = results.getInt("userId");
                String title = results.getString("title");
                String description = results.getString("description");
                String location = results.getString("location");
                String contact = results.getString("contact");
                String type = results.getString("type");
                String url = results.getString("url");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                Timestamp appointmentStart = results.getTimestamp("start");
                String aptStartDate = appointmentStart.toString();
                LocalDateTime startLDT = LocalDateTime.parse(aptStartDate, formatter);
                startLDT = convertUTCtoLocal(startLDT);
                aptStartDate = startLDT.toString();
                
                Timestamp appointmentEnd = results.getTimestamp("end");
                String aptEndDate = appointmentEnd.toString();
                LocalDateTime endLDT = LocalDateTime.parse(aptEndDate, formatter);
                endLDT = convertUTCtoLocal(endLDT);
                aptEndDate = endLDT.toString();

                
                Appointment currentAppointment = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type,url, aptStartDate, aptEndDate);
                displayAppointments.add(currentAppointment);
            }
            
        }
        catch(Exception e) {
            System.out.println("Error retrieving appointments in range " + startDate + " to " + endDate + ": " + e.getLocalizedMessage());
        }
        DBConnection.closeConnection();
        return displayAppointments;
    }
    
    public static ObservableList<Appointment> getCustAptAptInRange(Date startDate, Date endDate, int custId) throws SQLException, Exception {
        ObservableList<Appointment> displayAppointments = FXCollections.observableArrayList();
        String query = "select * from appointment where start >= '" + startDate + "' and end <= '" + endDate + "' and customerId = " + custId;
        System.out.println("Query to retrieve appointments in range: " + query);
        
        try {
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            
            while(results.next()) {
                int appointmentId = results.getInt("appointmentId");
                int customerId = results.getInt("customerId");
                int userId = results.getInt("userId");
                String title = results.getString("title");
                String description = results.getString("description");
                String location = results.getString("location");
                String contact = results.getString("contact");
                String type = results.getString("type");
                String url = results.getString("url");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                Timestamp appointmentStart = results.getTimestamp("start");
                String aptStartDate = appointmentStart.toString();
                LocalDateTime startLDT = LocalDateTime.parse(aptStartDate, formatter);
                startLDT = convertUTCtoLocal(startLDT);
                aptStartDate = startLDT.toString();
                
                Timestamp appointmentEnd = results.getTimestamp("end");
                String aptEndDate = appointmentEnd.toString();
                LocalDateTime endLDT = LocalDateTime.parse(aptEndDate, formatter);
                endLDT = convertUTCtoLocal(endLDT);
                aptEndDate = endLDT.toString();

                
                Appointment currentAppointment = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type,url, aptStartDate, aptEndDate);
                displayAppointments.add(currentAppointment);
            }
            
        }
        catch(Exception e) {
            System.out.println("Error retrieving appointments in range " + startDate + " to " + endDate + ": " + e.getLocalizedMessage());
        }
        DBConnection.closeConnection();
        return displayAppointments;
    }
    
    public static boolean checkAptIn15Min() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = convertLocaltoUTC(now);
        LocalDateTime soon = now.plusMinutes(15);
        soon = convertLocaltoUTC(soon);
        
        String query = "select * from appointment where start >= '" + formatter.format(now) + "' and "
                + "start <= '" + formatter.format(soon) + "'";
        System.out.println("Query to retrieve appointments in range: " + query);   
        try {
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            System.out.println("Error retrieving appointments in next 15 minutes: " + e.getLocalizedMessage());
        }
        return false;
    }
    
    public static boolean isAptOverlap(Appointment newAppointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        String ldtStart = newAppointment.getAptStart();
        LocalDateTime localDateStart = LocalDateTime.parse(ldtStart, formatter);
        localDateStart = convertLocaltoUTC(localDateStart);
        ldtStart = localDateStart.format(formatter);
        
        String ldtEnd = newAppointment.getAptEnd();
        LocalDateTime localDateEnd = LocalDateTime.parse(ldtEnd, formatter);
        localDateEnd = convertLocaltoUTC(localDateEnd);
        ldtEnd = localDateEnd.format(formatter);
        
        String query = "SELECT COUNT(*) FROM appointment WHERE"
            + "(start <= '" + ldtStart + "' AND end > '" + ldtStart + "')"
            + "OR (start < '" + ldtEnd + "' AND end > '" + ldtStart + "')";
        System.out.println("Query to check for overlapping appointments: " + query);
        
        try {
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            ResultSet results = statement.executeQuery(query);
            results.next();
            if(results.getInt("COUNT(*)") != 0) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            System.out.println("Error retrieving overlapping appointments: " + e.getLocalizedMessage());
        }
        return true;
    }
    
}
