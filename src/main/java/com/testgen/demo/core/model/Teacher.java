package com.testgen.demo.core.model;

import java.io.File;

public class Teacher {

    private String name, surname, email;
    private File[] tests;
    private int teacherID;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTests(File[] tests) {
        this.tests = tests;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public File[] getTests() {
        return tests;
    }

    public int getTeacherID() {
        return teacherID;
    }
}
