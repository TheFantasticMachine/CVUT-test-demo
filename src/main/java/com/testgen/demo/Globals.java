package com.testgen.demo;

import com.testgen.demo.core.model.Subject;

import java.io.File;
import java.util.ArrayList;

public class Globals {

    // user
    private static String userName;
    private static String userSurname;
    private static String userEmail;

    public String getUserName() { return userName; }
    public String getUserSurname() { return userSurname; }
    public String getUserEmail() { return userEmail; }

    public void setUserName (String name) { userName = name; }
    public void setUserSurname (String surname) { userSurname = surname; }
    public void setUserEmail (String email) { userEmail = email; }

    // logs
    private static File theadLog;

    public File getTheadLog() { return theadLog; }
    public void setTheadLog(File file) { theadLog = file; }

    // subjects
    private static ArrayList<Subject> allSubjects;

    public ArrayList<Subject> getAllSubjects() { return allSubjects; }

    public void setAllSubjects(ArrayList<Subject> allSubjects) { Globals.allSubjects = allSubjects; }
}
