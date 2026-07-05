package com.testgen.demo.core.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Category {

    private String categoryName;
    private int categoryID;
    @JsonAlias({"questions", "myQuestions", "questionSet"})
    private ArrayList<Question> questions;
    @JsonIgnore
    private Subject parentSubject;

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    @JsonIgnore
    public void setParentSubject(Subject parentSubject) {
        this.parentSubject = parentSubject;
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
    @JsonIgnore
    public Subject getParentSubject() {
        return parentSubject;
    }

    @JsonIgnore
    public List<Question> getUniqueQuestions(int amountNeeded) {
        List<Question> selectionOutput = new ArrayList<>();
        List<Question> temp = new ArrayList<>();
        for (Question question : this.getQuestions()) {
            temp.add(question);
        }
        Random random = new Random();

        for (int i = 0; i < amountNeeded; i++) {
            // -- pick a random question
            Question picked = temp.get(random.nextInt(temp.size()));
            selectionOutput.add(picked);
            temp.remove(picked);
        }

        return selectionOutput;
    }
}
