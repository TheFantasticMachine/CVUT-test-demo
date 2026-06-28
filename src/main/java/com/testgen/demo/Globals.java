package com.testgen.demo;

import com.testgen.demo.core.model.Subject;

import java.io.File;
import java.util.HashMap;

public class Globals {

    // user
    private static String userName, userSurname, userEmail, currentSession;

    public static String getCurrentSession() {
        return currentSession;
    }
    public static String getUserName() { return userName; }
    public static String getUserSurname() { return userSurname; }
    public static String getUserEmail() { return userEmail; }

    public static void setCurrentSession(String currentSession) {
        Globals.currentSession = currentSession;
    }
    public void setUserName (String name) { userName = name; }
    public void setUserSurname (String surname) { userSurname = surname; }
    public void setUserEmail (String email) { userEmail = email; }

    // logs
    private static File theadLog;

    public File getTheadLog() { return theadLog; }
    public void setTheadLog(File file) { theadLog = file; }

    // subjects
    private static HashMap<String, Subject> allSubjects;

    public HashMap<String,Subject> getAllSubjects() { return allSubjects; }

    public void setAllSubjects(HashMap<String, Subject> allSubjects) { Globals.allSubjects = allSubjects; }
}
