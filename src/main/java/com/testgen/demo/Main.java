package com.testgen.demo;

import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.threads.QuestionSync;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean loggedIn = false;
        while (!loggedIn) {
            // user login
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name:");
            String tempName = scanner.nextLine().trim().toLowerCase(); // name
            System.out.print("Enter your surname:");
            String tempSurname = scanner.nextLine().trim().toLowerCase(); // surname
            System.out.print("Enter your email:");
            String tempEmail = scanner.nextLine().trim().toLowerCase(); // email

            scanner.close();

            // use database to setup user
            try {
                DatabaseLoader databaseLoader = new DatabaseLoader();
                Connection db = databaseLoader.getConnector();

                String sql = "SELECT * FROM teachers WHERE name = '" + tempName + "' AND surname = '" + tempSurname + "' AND email = '" + tempEmail + "'";
                Statement statement = db.createStatement();
                ResultSet resultSet = statement.executeQuery(sql); // get the result
                // if there is some it means the user exists
                if (resultSet.next()) {
                    // we will use a file to share globals

                    // start sync threads
                    QuestionSync questionSync = new QuestionSync();
                    Thread questionSyncThread = new Thread(questionSync);
                    questionSyncThread.start();


                    // start loading last test
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
