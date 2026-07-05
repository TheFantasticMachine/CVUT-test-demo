package com.testgen.demo;

import com.testgen.demo.core.config.Settings;
import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.engine.Test;
import com.testgen.demo.core.model.Subject;
import com.testgen.demo.core.threads.ThreadManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SpringBootApplication
public class Main {
    static Globals globals = new Globals();

    public static void main(String[] args) { // FIXED: Changed 'static void main' to standard 'public static void main'
        SpringApplication.run(Main.class, args);

        boolean loggedIn = false;
        Scanner scanner = new Scanner(System.in);

        while (!loggedIn) {
            // User login configuration loading
            InputStream configStream = Main.class.getResourceAsStream("/config/settings.json");

            if (configStream == null) {
                try {
                    throw new FileNotFoundException("Could not find settings.json inside the project resources!");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            Settings settings = mapper.readValue(configStream, Settings.class);

            String tempName = "", tempSurname = "", tempEmail = "";
            if (settings.rememberMe) {
                tempName = settings.userData[0];
                tempSurname = settings.userData[1];
                tempEmail = settings.userData[2];
            }

            if (tempName.isEmpty() && tempSurname.isEmpty() && tempEmail.isEmpty()) {
                System.out.print("Enter your name: ");
                tempName = scanner.nextLine().trim().toLowerCase();
                System.out.print("Enter your surname: ");
                tempSurname = scanner.nextLine().trim().toLowerCase();
                System.out.print("Enter your email: ");
                tempEmail = scanner.nextLine().trim().toLowerCase();
            }

            // Authenticate user via database connections
            try (Connection db = DatabaseLoader.getConnector()) {
                String sql = "SELECT * FROM teachers WHERE name = '" + tempName + "' AND surname = '" + tempSurname + "' AND email = '" + tempEmail + "'";

                try (Statement statement = db.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {

                    if (resultSet.next()) {
                        globals.setUserName(resultSet.getString("name"));
                        globals.setUserSurname(resultSet.getString("surname"));
                        globals.setUserEmail(resultSet.getString("email"));
                        System.out.println("User logged in: " + Globals.getUserName() + " " + Globals.getUserSurname() + " | " + Globals.getUserEmail());

                        // Start thread manager background worker synchronization tasks
                        Thread questionSyncThread = new Thread(new ThreadManager());
                        questionSyncThread.setDaemon(false);
                        questionSyncThread.setName("threadManager");
                        questionSyncThread.start();

                        loggedIn = true;
                    } else {
                        System.out.println("Invalid teacher credentials entered. Please try again.\n");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        scanner.close();

        // FIXED: Test generation routine moved completely OUTSIDE the authentication loop space
        try {
            System.out.println("\n[SYSTEM] Waiting for initial background question synchronization to complete...");
            Thread.sleep(2000); // Give the background worker thread a moment to fetch data row sets

            var allSubjectsMap = Globals.getAllSubjects();

            // Defensive safeguard check to ensure maps are initialized before selection metrics
            if (allSubjectsMap != null && !allSubjectsMap.isEmpty()) {
                Random random = new Random();

                // FIXED: Convert Map values into an indexed list collection so numeric random slicing works!
                List<Subject> subjectList = new ArrayList<>(allSubjectsMap.values());
                Subject targetSubject = subjectList.get(random.nextInt(subjectList.size()));

                System.out.println("[ENGINE] Selected Target Subject Track: " + targetSubject.getSubjectName());

                // Fire up your custom test generator mechanism safely
                Test test = new Test(10, 3, 3, targetSubject);
                test.createTest();
            } else {
                System.err.println("[CRITICAL] Failed to execute generation: Global subject cache repository is empty.");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}