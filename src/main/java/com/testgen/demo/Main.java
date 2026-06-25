package com.testgen.demo;

import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.config.Settings;
import com.testgen.demo.core.threads.ThreadManager;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
        Scanner scanner = new Scanner(System.in);
        while (!loggedIn) {
            // user login
            String tempName = "", tempSurname = "", tempEmail = "";
            // opt 1 settings
            InputStream configStream = Main.class.getResourceAsStream("/com/testgen/demo/config/settings.json");

            if (configStream == null) {
                try {
                    throw new FileNotFoundException("Could not find settings.json inside the project resources!");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            Settings settings = mapper.readValue(configStream, Settings.class);

            if (settings.rememberMe) {
                tempName = settings.userData[0];
                tempSurname = settings.userData[1];
                tempEmail = settings.userData[2];
            }
            // opt 2 manually
            if (tempName.isEmpty() && tempSurname.isEmpty() && tempEmail.isEmpty()) {
                System.out.print("Enter your name:");
                tempName = scanner.nextLine().trim().toLowerCase(); // name
                System.out.print("Enter your surname:");
                tempSurname = scanner.nextLine().trim().toLowerCase(); // surname
                System.out.print("Enter your email:");
                tempEmail = scanner.nextLine().trim().toLowerCase(); // email
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
                    Thread questionSyncThread = new Thread(new ThreadManager());
                    questionSyncThread.setDaemon(false);
                    questionSyncThread.setName("threadManager");
                    questionSyncThread.start();

                    loggedIn = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        scanner.close();
    }
}