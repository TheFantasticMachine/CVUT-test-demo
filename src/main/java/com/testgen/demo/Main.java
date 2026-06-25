package com.testgen.demo;

import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.config.Settings;
import com.testgen.demo.core.threads.ThreadManager;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    static Helper help = new Helper();
    static Globals globals = new Globals();

    public static void main(String[] args) {
        boolean loggedIn = false;
        while (!loggedIn) {
            // user login
            String tempName = "", tempSurname = "", tempEmail = "";
            // opt 1 settings
            ObjectMapper mapper = new ObjectMapper();
            File settingsFile = new File(help.getConfigFile("settings"));
            Settings settings = mapper.readValue(settingsFile, Settings.class);
            if (settings.rememberMe) {
                tempName = settings.userData[0];
                tempSurname = settings.userData[1];
                tempEmail = settings.userData[2];
            }
            // opt 2 manually
            if (tempName.isEmpty() && tempSurname.isEmpty() && tempEmail.isEmpty()) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter your name:");
                tempName = scanner.nextLine().trim().toLowerCase(); // name
                System.out.print("Enter your surname:");
                tempSurname = scanner.nextLine().trim().toLowerCase(); // surname
                System.out.print("Enter your email:");
                tempEmail = scanner.nextLine().trim().toLowerCase(); // email

                scanner.close();
            }

            // use database to setup user
            try {
                DatabaseLoader databaseLoader = new DatabaseLoader();
                Connection db = databaseLoader.getConnector();

                String sql = "SELECT * FROM teachers WHERE name = '" + tempName + "' AND surname = '" + tempSurname + "' AND email = '" + tempEmail + "'";
                Statement statement = db.createStatement();
                ResultSet resultSet = statement.executeQuery(sql); // get the result
                // if there is some it means the user exists
                if (resultSet.next()) {
                    // set up globals
                    globals.setUserName(resultSet.getString("name"));
                    globals.setUserSurname(resultSet.getString("surname"));
                    globals.setUserEmail(resultSet.getString("email"));
                    System.out.println("User logged in: " + globals.getUserName() + " " + globals.getUserSurname() + " | " + globals.getUserEmail());

                    // start thread manager
                    ThreadManager threadManager = new ThreadManager();
                    Thread questionSyncThread = new Thread(threadManager);
                    questionSyncThread.start();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
