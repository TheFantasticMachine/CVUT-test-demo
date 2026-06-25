package com.testgen.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Helper {

    public String getConfigFile (String filename) { return "/config/" + filename +".json"; }
    public String getLogFile (String filename) { return "/src/main/resources/com/testgen/demo/logs/" + filename +".txt"; }

//    public String getLineInFileThatHas (File file, String searchedString) {
//        try {
//            Scanner fileReader = new Scanner(file);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
