package com.testgen.demo.core.config;

import com.testgen.demo.Globals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {

    public static void createFile(String filepath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));

            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);

            Globals globals = new Globals();
            writer.write("File created at " + formattedDate + " by " + globals.getUserName() + " " + globals.getUserSurname());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
