package com.testgen.demo.core.model;

import java.util.ArrayList;

public class Question {
    private int questionID;
    private String questionText;
    private String correct;
    private ArrayList<String> wrong;

    public Question(int questionID, String questionText, String correct, ArrayList<String> wrong) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.correct = correct;
        this.wrong = wrong;
    }

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
}
