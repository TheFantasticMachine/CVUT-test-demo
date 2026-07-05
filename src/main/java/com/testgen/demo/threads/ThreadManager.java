package com.testgen.demo.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.config.Settings;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadManager implements Runnable{
    private FileHandler fileHandler = new FileHandler();
    @Override
    public void run() {
        try {
            // start log
            String logPath = fileHandler.getLogFile("threads");
            System.out.println(logPath);
            System.out.println(fileHandler.getConfigFile("settings"));

            fileHandler.createFile(new String[] {logPath, fileHandler.getConfigFile("settings")});
            File log = new File(fileHandler.getLogFile("threads"));

            Globals globals = new Globals();
            globals.setTheadLog(log);

            // start first sync
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(now);

            Thread initialSync = new Thread(new QuestionSync());
            initialSync.setName("QuestionSync_Init");
            initialSync.start();

                // time to decide what to do
                while (true) {
                    System.out.println("Manager check");

                    now = LocalDateTime.now(); // get current time

                    String lastInstance = fileHandler.findInFile(globals.getTheadLog(), "started: question sync");
                    String lastInstanceTimeAsString = lastInstance.substring(0, lastInstance.indexOf("]")).replace("[", "").trim();

                    // FIX: Pass the custom formatter pattern directly into the parser argument line
                    LocalDateTime lastInstanceTime = LocalDateTime.parse(lastInstanceTimeAsString, dateTimeFormatter);

                    if (!lastInstanceTime.plusMinutes(10).isAfter(now)) {
                        System.out.println("Starting Question sync");
                        // FIX: Create a fresh Thread container slot instead of re-invoking the old dead instance
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
                        int testFileCount = 0;

                        for (File file : files) {
                            System.out.println(file);
                            testFileCount++;
                        }

                        if (settings.saveLastTests > testFileCount) {
                            // start
                            Thread testDownloadThread = new Thread(new LoadTests());
                            testDownloadThread.setDaemon(true);
                            testDownloadThread.setName("LoadTests");
                            testDownloadThread.start();
                        }
                    }


                    // wait 2 minutes to check again
                    Thread.sleep(2 * 60 * 1000);
                }
            } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
