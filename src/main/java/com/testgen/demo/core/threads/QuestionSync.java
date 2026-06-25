package com.testgen.demo.core.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.Helper;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.engine.DatabaseLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuestionSync implements Runnable{
    private Globals globals = new Globals();
    private FileHandler fileHandler = new FileHandler();
    @Override
    public void run() {
        try {
            // write to log
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fileHandler.write(globals.getTheadLog(), "[" + now.format(dateTimeFormatter) + "] started: question sync");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
