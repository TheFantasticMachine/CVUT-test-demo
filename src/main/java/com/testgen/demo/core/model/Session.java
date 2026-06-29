package com.testgen.demo.core.model;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private String sessionCode;
    private List<SessionSubject> subjects = new ArrayList<>();

    public Session() {}

    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    public List<SessionSubject> getSubjects() { return subjects; }
    public void setSubjects(List<SessionSubject> subjects) { this.subjects = subjects; }
}

class SessionSubject {
    private int subjectID;
    private String subjectName;
    private List<SessionCategory> categories = new ArrayList<>();

    public SessionSubject() {}

    public int getSubjectID() { return subjectID; }
    public void setSubjectID(int subjectID) { this.subjectID = subjectID; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public List<SessionCategory> getCategories() { return categories; }
    public void setCategories(List<SessionCategory> categories) { this.categories = categories; }
}

class SessionCategory {
    private int categoryID;
    private String categoryName;
    private List<Integer> usedQuestions = new ArrayList<>(); // Keeps track of ID primitives purely

    public SessionCategory() {}

    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public List<Integer> getUsedQuestions() { return usedQuestions; }
    public void setUsedQuestions(List<Integer> usedQuestions) { this.usedQuestions = usedQuestions; }
}