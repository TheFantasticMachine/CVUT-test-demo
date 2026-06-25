package com.testgen.demo.core.model;

import java.io.File;

public class Subject {

    private Category[] categories;
    private File configFile;

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }
    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public File getConfigFile() { return configFile; }
    public Category[] getCategories() { return categories; }
}
