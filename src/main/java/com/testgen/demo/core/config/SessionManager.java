package com.testgen.demo.core.config;

import com.testgen.demo.Helper;
import com.testgen.demo.core.model.Category;
import com.testgen.demo.core.model.Subject;

import java.io.File;

public class SessionManager {

    public String startSession() {
        String sessionCode = Helper.generateSessionCode(10);
        FileHandler.createFile(new String[] {FileHandler.getConfigFile("session-" + sessionCode)});

        return sessionCode;
    }
}

class SessionData {
    String sessionCode;
    Subject[] subjects;
}

class SessionDataSubject {
    int subjectID;
    String subjectName;
    Category[] categories;
}

class SessionDataCategory {
    int categoryID;
    String categoryName;
    int[] usedQuestions;
}
