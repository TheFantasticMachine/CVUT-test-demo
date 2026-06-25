package com.testgen.demo.core.threads;

import com.testgen.demo.Helper;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.engine.DatabaseLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestionSync implements Runnable{
    private Helper help = new Helper();
    @Override
    public void run() {
        try {
            // first get db
            DatabaseLoader databaseLoader = new DatabaseLoader();
            Connection db = databaseLoader.getConnector();

            // get name of all subjects
            Statement statement = db.createStatement();
            String sql = "select name from subjects";
            ResultSet subjectNames = statement.executeQuery(sql);

            // use while to update and or create new config files for subjects
            while (subjectNames.next()) {
                try {
                    String currentSubjectName = subjectNames.getString("name");
                    File file = new File(FileHandler.getConfigFile("subject_" + currentSubjectName));

                    // Try to create the file
                    if (file.createNewFile()) {
                        // file doesnt exists - its created ... load all questions
                        System.out.println("File created: " + file.getName());
                        sql = "select * from questions where subject = \''" + currentSubjectName+ "\''";
                    } else {
                        System.out.println("File already exists.");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
