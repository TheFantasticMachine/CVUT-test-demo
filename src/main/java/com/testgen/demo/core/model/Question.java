package com.testgen.demo.core.model;

public class Question {
    private int questionID;
    private String questionText;
    private String correct;
    private String[] wrong;

    public Question(int questionID, String questionText, String correct, String[] wrong) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.correct = correct;
        this.wrong = wrong;
    }
}
