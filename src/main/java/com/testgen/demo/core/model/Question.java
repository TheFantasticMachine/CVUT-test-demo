package com.testgen.demo.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    @JsonIgnore
    public List<String> giveAnswers() {
        List<String> result = new ArrayList<>();
        result.add(correct);
        List<String> temp = new ArrayList<>();

        for (String wrong : this.getWrong()) {
            temp.add(wrong);
        }

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            String wrong = temp.get(random.nextInt(temp.size()));
            result.add(wrong);
            temp.remove(wrong);
        }

        Collections.shuffle(result);
        return result;
    }
}
