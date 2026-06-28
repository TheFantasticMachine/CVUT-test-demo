package com.testgen.demo.core.model;

import com.testgen.demo.core.engine.DatabaseLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class Category {

    private String categoryName;
    private int categoryID;
    private ArrayList<Question> questions;

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public int getCategoryID() {
        return categoryID;
    }
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public Question getQuestion() {
        Question selected = null;
        try {
            Connection connection = DatabaseLoader.getConnector();
            Statement statement = connection.createStatement();
            String sql;

            boolean picked = false;
            while (!picked) {
                Random random = new Random();
                // pick a question
                selected = questions.get(random.nextInt(questions.size()));
                // test if current session already picked it

                // repick if necessary

                // add to session data
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selected;
    }
}
