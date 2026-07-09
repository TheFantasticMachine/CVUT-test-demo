package com.testgen.demo.core.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "question", "answers", "correctIndex" })
public class TestQuestion {
    @JsonAlias ({ "question" })
    private String question;
    private List<String> answers;
    private int correctIndex;
    @JsonIgnore
    private String correctAnswer;

    public TestQuestion(String question, List<String> answers, int correctIndex) {
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;

        this.correctAnswer = answers.get(correctIndex);
    }

    public String getCorrectAnswer() {
        if (this.answers != null && this.correctIndex >= 0 && this.correctIndex < this.answers.size()) {
            return this.answers.get(this.correctIndex);
        }
        return "No correct answer mapped";
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }

    public int getCorrectIndex() { return correctIndex; }
    public void setCorrectIndex(int correctIndex) { this.correctIndex = correctIndex; }
}