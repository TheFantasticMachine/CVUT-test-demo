package com.testgen.demo.core.model;

import java.util.ArrayList;

public class Question {
    private int questionID;
    private String questionText;
    private String correct;
    private ArrayList<String> wrong;

    public int getQuestionID() {
        return questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrect() {
        return correct;
    }

    public ArrayList<String> getWrong() {
        return wrong;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public void setWrong(ArrayList<String> wrong) {
        this.wrong = wrong;
    }
}
