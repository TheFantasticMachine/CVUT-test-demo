package com.testgen.demo.core.threads;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.testgen.demo.Globals;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.config.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadManager implements Runnable {
    private FileHandler fileHandler = new FileHandler();

    @Override
    public void run() {
        try {
            String logPath = fileHandler.getLogFile("threads");
            fileHandler.createFile(new String[] {logPath, fileHandler.getConfigFile("settings")});
            File log = new File(fileHandler.getLogFile("threads"));

            Globals globals = new Globals();
            globals.setTheadLog(log);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            // Fire off the initial background sync
            Thread initialSync = new Thread(new QuestionSync());
            initialSync.setName("QuestionSync_Init");
            initialSync.start();

            while (true) {
                System.out.println("Manager check");
                now = LocalDateTime.now();

                String lastInstance = fileHandler.findInFile(globals.getTheadLog(), "started: question sync");
                boolean triggerSync = false;

                // FIXED: Safety guard check to handle empty or freshly created log files safely
                if (lastInstance == null || lastInstance.isEmpty() || !lastInstance.contains("]")) {
                    triggerSync = true;
                } else {
                    String lastInstanceTimeAsString = lastInstance.substring(0, lastInstance.indexOf("]")).replace("[", "").trim();
                    LocalDateTime lastInstanceTime = LocalDateTime.parse(lastInstanceTimeAsString, dateTimeFormatter);
                    if (!lastInstanceTime.plusMinutes(10).isAfter(now)) {
                        triggerSync = true;
                    }
                }

                if (triggerSync) {
                    System.out.println("Starting periodic Question sync");
                    Thread periodicSync = new Thread(new QuestionSync());
                    periodicSync.setName("QuestionSync_Loop");
                    periodicSync.start();
                }

                ObjectMapper mapper = new ObjectMapper();
                Settings settings = mapper.readValue(new File(fileHandler.getConfigFile("settings")), Settings.class);
                File configDir = new File("config");

                File[] files = configDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.contains("test");
                    }
                });

                if (files != null) {
                    int testFileCount = files.length;
                    if (settings.saveLastTests > testFileCount) {
                        Thread testDownloadThread = new Thread(new LoadTests());
                        testDownloadThread.setDaemon(true);
                        testDownloadThread.setName("LoadTests");
                        testDownloadThread.start();
                    }
                }

                // Wait 2 minutes to evaluate criteria cycles again
                Thread.sleep(2 * 60 * 1000);
            }
        } catch (InterruptedException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}