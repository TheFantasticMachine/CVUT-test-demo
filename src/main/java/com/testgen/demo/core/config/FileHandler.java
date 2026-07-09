package com.testgen.demo.core.config;

import java.io.*;

public class FileHandler {
    private static final String workingDir = "src/main/resources/";

    // Clean relative project paths (no leading slashes)
    public static String getConfigFile(String filename) {
        return workingDir + "config/" + filename + ".json";
    }
    public String getLogFile(String filename) {
        return workingDir + "logs/" + filename + ".txt";
    }
    public static String getTestJson (String filename) { return workingDir + "tests/" + filename + ".json"; }
    public String getHtmlFile (String filename) { return workingDir + "templates/" + filename + ".html"; }

    public static String getWorkingDir() { return workingDir; }

    public static void createFile(String[] filenames) {
        for (int i = 0; i < filenames.length; i++) {
            String filepath = workingDir + filenames[i];

            if (filepath.indexOf(workingDir) != filepath.lastIndexOf(workingDir)) {
                filepath = filenames[i];
            }
            File file = new File(filepath);
            if (!file.exists()) {
                try {
                    if (file.getParentFile() != null) {
                        file.getParentFile().mkdirs();
                    }

                    boolean doesExist = file.createNewFile();
                    if (!doesExist) {
                        System.out.println("New File made: " + file.getPath());
                    } else {
                        System.out.println("The file " + file.getName() + " already exists");
                    }
                } catch (IOException e) {
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

    public static void write(File file, String text) {
        try {
            if (!file.exists()) { createFile( new String[] {file.getPath()} ); }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(text);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeJSON(File file, String[] json) {
        for (String jsonString : json) {
            write(file, jsonString);
        }
    }

    public static String findInFile(File file, String find) {
        String output = "";
        try (BufferedReader reader = new BufferedReader( new FileReader(file) )) {
            // reading
            System.out.println("reading " + file.getPath());
            System.out.println("\t --- <" + file.getName() + "> ---");

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                if (line.contains(find)) {
                    output = line;
                }
            }

            System.out.println("\t --- < end > ---");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
