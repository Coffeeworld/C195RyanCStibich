/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author coffeeworld
 */
public class Logger {
    public static final String fileName = "log.txt";
    
    public static void createLog(String logNote) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            PrintWriter logFile = new PrintWriter(fw);
            logFile.println(logNote);
            logFile.close();
            System.out.println(logNote + " - has been written to the log");
        } catch (IOException ex) {
            System.out.println("Logger error: " + ex.getMessage());
        }
    }
    
}
