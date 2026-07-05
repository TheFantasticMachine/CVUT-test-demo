package com.testgen.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "creator", "timestamp", "variant", "subjectName", "categories", "questions" })
public class TestData {
    @JsonAlias ({"creator"})
    private String creator;
    @JsonAlias ({"subject"})
    private String subjectName;
    @JsonAlias ({"timestamp"})
    private String timestamp;
    @JsonAlias ({"categories"})
    private List<String> categories;
    @JsonAlias ({"variant"})
    private int variant;
    @JsonAlias ({"questions"})
    private List<TestQuestion> questions;

    public TestData() {}

    public TestData(String creator, String subjectName, String timestamp, int variant, List<TestQuestion> questions, List<String> categories) {
        this.creator = creator;
        this.subjectName = subjectName;
        this.timestamp = timestamp;
        this.categories = categories;
        this.variant = variant;
        this.questions = questions;
    }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public int getVariant() { return variant; }
    public void setVariant(int variant) { this.variant = variant; }

    public List<TestQuestion> getQuestions() { return questions; }
    public void setQuestions(List<TestQuestion> questions) { this.questions = questions; }
}