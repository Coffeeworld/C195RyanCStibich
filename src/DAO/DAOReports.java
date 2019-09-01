/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;

/**
 *
 * @author coffeeworld
 */
public class DAOReports {
    public static ObservableList<AppointmentTypeReport> getAppointmentTypeCount() {
        ObservableList<AppointmentTypeReport> appointmentTypeReport = FXCollections.observableArrayList();
        try {
            String query = "SELECT YEAR(start), MONTH(start), type, COUNT(type) "
                    + "FROM appointment GROUP BY type ORDER BY YEAR(start), MONTH(start) DESC";
            DBConnection.makeConnection();
            Statement statement = DBConnection.getConn().createStatement();
            //create query to retrieve user information from database
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                int year = results.getInt("YEAR(start)");
                int month = results.getInt("MONTH(start)");
                String type = results.getString("type");
                int typeCount = results.getInt("COUNT(type)");
                
                AppointmentTypeReport currentRecord = new AppointmentTypeReport(typeCount, year, month, type);
                appointmentTypeReport.add(currentRecord);
            }
            DBConnection.closeConnection();
        } catch (SQLException ex) {
            System.out.println("Error generating appointment type report ObservableList: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error generating appointment type report ObservableList: " + ex.getMessage());
        }
        return appointmentTypeReport;
    }
    
    static public class AppointmentTypeReport {
        private int typeCount;
        private int year;
        private int month;
        private String type;

        public AppointmentTypeReport(int typeCount, int year, int month, String type) {
            this.typeCount = typeCount;
            this.year = year;
            this.month = month;
            this.type = type;
        }

        public int getTypeCount() {
            return typeCount;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public String getType() {
            return type;
        }
          
    }
}
