package com.testgen.demo;

import com.testgen.demo.core.model.Subject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static HashMap<String, Subject> allSubjects = null;

    public static HashMap<String,Subject> getAllSubjects() { return allSubjects; }

    public void setAllSubjects(HashMap<String, Subject> allSubjects) { Globals.allSubjects = allSubjects; }

    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static ObjectMapper getMapper() {
        return mapper;
    }

    private static final List<Integer> usedQuestionIDs = new ArrayList<>();

    public static List<Integer> getUsedQuestionIDs() {
        return usedQuestionIDs;
    }

    public static void markQuestionAsUsed(int id) {
        if (!usedQuestionIDs.contains(id)) {
            usedQuestionIDs.add(id);
        }
    }
}
