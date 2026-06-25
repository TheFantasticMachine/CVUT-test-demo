package com.testgen.demo;

import java.io.File;
import java.io.IOException;

public class Helper {

    public static String getConfigFile(String filename) { return "/config/" + filename +".json"; }
    public String getLogFile (String filename) { return "/logs/" + filename +".txt"; }

    public void createNewFile(String filepath) {
        try {
            File myObj = new File(filepath); // Create File object
            if (myObj.createNewFile()) {           // Try to create the file
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); // Print error details
        }
    }
}
