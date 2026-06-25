package com.testgen.demo.core.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.model.Subject;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
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
            fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] started: question sync");

            // 1) get all subjects as raw result set
            Connection connection = DatabaseLoader.getConnector();
            Statement statement = connection.createStatement();

            String sql = "select name from subjects";
            ResultSet rawSubjects = statement.executeQuery(sql);
            Subject[] subjects = null;

            sql = "select count(1) from subjects";
            ResultSet numberInfoRaw = statement.executeQuery(sql);
            numberInfoRaw.next();
            int subjectsTotal = numberInfoRaw.getInt(1);
            int subjectCurrentIndex = 0;

            // 2) start the full setup
            while (rawSubjects.next()) {
                subjectCurrentIndex++;

                // 2.1 load the name
                String subjectName = rawSubjects.getString("name");
                SubjectConfig subjectConfigClass = new SubjectConfig();
                subjectConfigClass.subjectSQLName = subjectName;

                sql = "select subjectID from subjects where name = \'" + subjectName + "\'";
                ResultSet resultSet = statement.executeQuery(sql);
                resultSet.next();
                int subjectID = rawSubjects.getInt("subjectID");
                subjectConfigClass.subjectID = subjectID;

                subjectName = subjectName.replaceAll(" ", "-").toLowerCase().trim();
                subjectConfigClass.subjectName = subjectName;

                // 2.2 load and update the config
                fileHandler.createFile(new String[] {fileHandler.getConfigFile(subjectName + File.separator + subjectName + "-config")});
                Subject subject = new Subject();
                subject.setConfigFile( new File(fileHandler.getConfigFile(subjectName + File.separator + subjectName + "-config")) );

                // report
                now = LocalDateTime.now();
                fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: " + subjectName + " done (" + Math.floor ((subjectCurrentIndex / subjectsTotal) * 100) + "%)") ;
            }

            now = LocalDateTime.now();
            fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: 100% done");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SubjectConfig {
    public int subjectID;
    public String subjectSQLName;
    public String subjectName;
    public String[] categories;
}