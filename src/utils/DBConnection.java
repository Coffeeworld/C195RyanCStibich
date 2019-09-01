/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author coffeeworld
 */
public class DBConnection {
    private static final String databaseName = "U04Uah";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + databaseName;
    private static final String userName = "U04Uah";
    private static final String password = "53688345305";
    private static final String driver = "com.mysql.jdbc.Driver";    
    static Connection conn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DB_URL, userName, password);
        System.out.println("Connection Successful");
    }
    
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        conn.close();
        System.out.println("Connection Closed");
    }

    public static Connection getConn() {
        return conn;
    }
    
    
}
