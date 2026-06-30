package com.testgen.demo.core.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.engine.DatabaseLoader;
import com.testgen.demo.core.model.Category;
import com.testgen.demo.core.model.Question;
import com.testgen.demo.core.model.Subject;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionSync implements Runnable {
    private Globals globals = new Globals();
    private FileHandler fileHandler = new FileHandler();
    private ObjectMapper mapper = globals.getMapper();

    @Override
    public void run() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] started: question sync");

        try (Connection connection = DatabaseLoader.getConnector()) {

            int subjectsTotal = 0;

            try (Statement countStatement = connection.createStatement();
                 ResultSet countResult = countStatement.executeQuery("SELECT COUNT(1) FROM subjects")) {
                if (countResult.next()) {
                    subjectsTotal = countResult.getInt(1);
                }
            }

            if (subjectsTotal == 0) {
                now = LocalDateTime.now();
                fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: Aborted (No subjects discovered in DB).");
                return;
            }

            int subjectCurrentIndex = 0;
            HashMap<String, Subject> subjects = new HashMap<>();
            String sql = "SELECT * FROM subjects";

            try (Statement loopStatement = connection.createStatement();
                 ResultSet rawSubjects = loopStatement.executeQuery(sql)) {

                while (rawSubjects.next()) {
                    Subject subject = new Subject();

                    String name = rawSubjects.getString("name");
                    String formattedSubjectName = name.replaceAll(" ", "-").toLowerCase();
                    subject.setSubjectName(formattedSubjectName);

                    fileHandler.createFile(new String[]{ fileHandler.getConfigFile(formattedSubjectName + "-config") });
                    subject.setConfigFile(new File(fileHandler.getConfigFile(formattedSubjectName + "-config")));

                    subject.setSubjectID(rawSubjects.getInt("subjectID"));
                    subject.setAdminID(rawSubjects.getInt("adminID"));

                    now = LocalDateTime.now();
                    subject.setLastSync(now);

                    subjects.put(formattedSubjectName, subject);

                    subjectCurrentIndex++;
                    double progressPercentage = Math.floor(((double) subjectCurrentIndex / subjectsTotal) * 100);
                    fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: " + formattedSubjectName + " initialized (" + (int) progressPercentage + "%)");
                }

                // Loop through each subject file configuration block
                for (Subject subject : subjects.values()) {
                    // Wrap each subject in an isolated try-catch so one bad subject doesn't kill the loop!
                    try {
                        System.out.println("\t -------< subject >-----");
                        System.out.println("- " + subject.getSubjectName() + " ... id: " + subject.getSubjectID());

                        int categoryTotal = 0;
                        sql = "SELECT COUNT(1) FROM categories where subjectID = " + subject.getSubjectID();

                        try (Statement countStatement = connection.createStatement();
                             ResultSet countResult = countStatement.executeQuery(sql)) {
                            if (countResult.next()) {
                                categoryTotal = countResult.getInt(1);
                                System.out.println("- categories:" + categoryTotal);
                            }
                        }

                        if (categoryTotal == 0) {
                            now = LocalDateTime.now();
                            // LOG ERROR: Write the skip directly to your log file!
                            fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] WARNING: Skipping " + subject.getSubjectName() + " (No categories found).");
                            continue; // Safely skip to the next subject
                        }

                        List<Category> categories = new ArrayList<>();
                        sql = "select * from categories where subjectID = " + subject.getSubjectID();

                        try (Statement categoriesStatement = connection.createStatement();
                             ResultSet rawCategories = categoriesStatement.executeQuery(sql)) {
                            while (rawCategories.next()) {
                                Category category = new Category();
                                category.setCategoryName(rawCategories.getString("categoryName"));
                                category.setCategoryID(rawCategories.getInt("categoryID"));
                                categories.add(category);
                            }
                        }

                        subject.setCategories((ArrayList<Category>) categories);

                        System.out.println("\t -------< categories >-----");
                        for (Category category : categories) {
                            int questionTotal = 0;
                            sql = "SELECT COUNT(1) FROM questions where categoryID = " + category.getCategoryID();

                            try (Statement countStatement = connection.createStatement();
                                 ResultSet countResult = countStatement.executeQuery(sql)) {
                                if (countResult.next()) {
                                    questionTotal = countResult.getInt(1);
                                    System.out.println(" - " + category.getCategoryName() + " ( " + questionTotal + " questions)");
                                }
                            }

                            // FIXED: Changed from 'return' to 'continue' so an empty category doesn't crash everything!
                            if (questionTotal == 0) {
                                now = LocalDateTime.now();
                                fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] WARNING: Empty category '" + category.getCategoryName() + "' skipped.");
                                continue;
                            }

                            ArrayList<Question> questions = new ArrayList<>();
                            sql = "select * from questions where categoryID = " + category.getCategoryID();

                            try (Statement questionsStatement = connection.createStatement();
                                 ResultSet rawQuestions = questionsStatement.executeQuery(sql)) {
                                while (rawQuestions.next()) {
                                    Question question = new Question();

                                    question.setQuestionID(rawQuestions.getInt("questionID"));
                                    question.setCorrect(rawQuestions.getString("correctAnswer"));
                                    question.setQuestionText(rawQuestions.getString("questionText"));

                                    String wrongString = rawQuestions.getString("otherAnswer");
                                    ArrayList<String> wrongAnswers = new ArrayList<>();
                                    if (wrongString != null && !wrongString.isEmpty()) {
                                        for (String item : wrongString.split("\\|")) {
                                            wrongAnswers.add(item.trim());
                                        }
                                    }
                                    question.setWrong(wrongAnswers);

                                    questions.add(question);
                                }
                            }
                            category.setQuestions(questions);
                        }

                        // Save the file
                        mapper.writeValue(subject.getConfigFile(), subject);

                    } catch (Exception subjectEx) {
                        // CRITICAL LOG: If anything fails inside this subject, record it and keep running!
                        now = LocalDateTime.now();
                        fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] CRITICAL ERROR syncing subject " + subject.getSubjectName() + ": " + subjectEx.getMessage());
                        subjectEx.printStackTrace();
                        continue; // Move to the next subject configuration
                    }
                }

            } catch (SQLException e) {
                now = LocalDateTime.now();
                fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] DATABASE ERROR: " + e.getMessage());
                throw new RuntimeException(e);
            }

            globals.setAllSubjects(subjects);

            now = LocalDateTime.now();
            fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] question sync: finished successfully");

        } catch (Exception e) {
            now = LocalDateTime.now();
            fileHandler.write(globals.getTheadLog(), "\n[" + now.format(dateTimeFormatter) + "] FATAL UNCAUGHT THREAD CRASH: " + e.getMessage());
            e.printStackTrace();
        }
    }
}