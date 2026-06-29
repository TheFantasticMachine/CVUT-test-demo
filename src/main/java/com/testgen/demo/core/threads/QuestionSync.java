package com.testgen.demo.core.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.model.Subject;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSync implements Runnable {
    private Globals globals = new Globals();
    private FileHandler fileHandler = new FileHandler();

    @Override
    public void run() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // Write initial boot trace out to the shared background thread log
        fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] started: question sync");

        // Use try-with-resources to ensure our database connections close automatically
        try (Connection connection = DatabaseLoader.getConnector()) {

            int subjectsTotal = 0;

            // FIX 1: Use an isolated statement block to calculate the row count ahead of the loop
            try (Statement countStatement = connection.createStatement();
                 ResultSet countResult = countStatement.executeQuery("SELECT COUNT(1) FROM subjects")) {
                if (countResult.next()) {
                    subjectsTotal = countResult.getInt(1);
                }
            }

            // If there are no subjects present in the system, exit early to prevent math anomalies
            if (subjectsTotal == 0) {
                now = LocalDateTime.now();
                fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: Aborted (No subjects discovered).");
                return;
            }

            int subjectCurrentIndex = 0;
            HashMap<String, Subject> subjects = new HashMap<>();

            // FIX 2: Pull BOTH the name and subjectID in a single row query to avoid nested loop queries
            String sql = "SELECT * FROM subjects";

            try (Statement loopStatement = connection.createStatement();
                 ResultSet rawSubjects = loopStatement.executeQuery(sql)) {

                while (rawSubjects.next()) {
                    subjectCurrentIndex++;
                    double progressPercentage = Math.floor(((double) subjectCurrentIndex / subjectsTotal) * 100);

                    // 1) get the subject name so we can start building it
                    Subject subject = new Subject();

                    String name = rawSubjects.getString("name");
                    String formattedSubjectName = name.replaceAll(" ", "-").toLowerCase();

                    now = LocalDateTime.now();
                    fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: " + formattedSubjectName + " done (" + (int) progressPercentage + "%)");
                }
            }

            globals.setAllSubjects(subjects);

            now = LocalDateTime.now();
            fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: finished");

        } catch (Exception e) {
            System.err.println("QuestionSync worker encountered an unrecoverable database stream execution error.");
            e.printStackTrace();
        }
    }
}