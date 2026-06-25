package com.testgen.demo.core.config;

import com.testgen.demo.Globals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static String workingDir = "src/main/resources/";

    // Clean relative project paths (no leading slashes)
    public static String getConfigFile(String filename) {
        return workingDir + "config/" + filename + ".json";
    }

    public String getLogFile(String filename) {
        return workingDir + "logs/" + filename + ".txt";
    }

    public static void createFile(String[] filenames) {
        for (int i = 0; i < filenames.length; i++) {
            File file = new File(workingDir + filenames[i]);

            try {
                boolean doesExist = file.createNewFile();
                if (!doesExist) {
                    System.out.println("New File made: " + file.getName());
                }
                else {
                    System.out.println("The file " + file.getName() + " already exists");
                }
            }
            catch (IOException e) {
                mkdir(file.getParent());
                try {
                    if (!file.createNewFile()) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static boolean mkdir(String dir) {
        // Address of Current Directory
        String currentDirectory = System.getProperty("user.dir");

        // Specify the path of the directory to be created
        String directoryPath = currentDirectory + File.separator + dir;

        // Create a File object representing the directory
        File directory = new File(directoryPath);

        // Attempt to create the directory
        return directory.mkdir();
    }

    public void write(File file, String text) {
        try {
            if (!file.exists()) { this.createFile( new String[] {file.getPath()} ); }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
