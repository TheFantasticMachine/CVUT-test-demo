package com.testgen.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Subject {

    @JsonIgnore
    private File configFile;
    @JsonIgnore
    private int adminID;
    private int subjectID;
    private String subjectName;
    private LocalDateTime lastSync;
    private ArrayList<Category> categories;
    @JsonIgnore
    public boolean isGettingSynced = false;

    public void setConfigFile(File configFile) { this.configFile = configFile; }
    public void setCategories(ArrayList<Category> categories) { this.categories = categories; }
    public void setAdminID(int adminID) { this.adminID = adminID; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setLastSync(LocalDateTime lastSync) { this.lastSync = lastSync; }
    public void setSubjectID(int subjectID) { this.subjectID = subjectID; }

    public File getConfigFile() { return configFile; }
    public ArrayList<Category> getCategories() { return categories; }
    public int getAdminID() { return adminID; }
    public String getSubjectName() { return subjectName; }
    public LocalDateTime getLastSync() { return lastSync; }
    public int getSubjectID() { return subjectID; }


}
