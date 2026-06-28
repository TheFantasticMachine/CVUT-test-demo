package com.testgen.demo.core.model;

import java.io.File;

public class Subject {

    private Category[] categories;
    private File configFile;
    private int adminID;
    private String subjectName;

    public void setConfigFile(File configFile) { this.configFile = configFile; }
    public void setCategories(Category[] categories) { this.categories = categories; }
    public void setAdminID(int adminID) { this.adminID = adminID; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }


    public File getConfigFile() { return configFile; }
    public Category[] getCategories() { return categories; }
    public int getAdminID() { return adminID; }
    public String getSubjectName() { return subjectName; }
}
