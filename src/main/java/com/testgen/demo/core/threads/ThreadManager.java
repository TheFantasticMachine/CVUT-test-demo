package com.testgen.demo.core.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.Helper;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.config.Settings;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class ThreadManager implements Runnable{
    private Helper help = new Helper();
    private FileHandler fileHandler = new FileHandler();
    @Override
    public void run() {
        try {
            // start log
            String logPath = fileHandler.getLogFile("threads");
            fileHandler.createFile(new String[] {logPath});
            File log = new File(fileHandler.getLogFile("threads"));

            Globals globals = new Globals();
            globals.setTheadLog(log);

            // start first sync
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            fileHandler.write(log,"[" + now.format(dateTimeFormatter) + "] started: question sync");
            Thread threadQuestionSync = new Thread(new QuestionSync());
            threadQuestionSync.setName("QuestionSync");
            threadQuestionSync.start();

                // time to decide what to do
                while (true) {
                    // we will sync questions every 30 min
                    now = LocalDateTime.now(); // get current time

                    String lastInstance = fileHandler.findInFile(globals.getTheadLog(), "started: question sync");
                    String lastInstanceTimeAsString = lastInstance.substring(0, lastInstance.indexOf("]")).trim();
                    LocalDateTime lastInstanceTime = LocalDateTime.parse(lastInstanceTimeAsString);

                    if (!lastInstanceTime.plusMinutes(30).isAfter(now)) {
                        threadQuestionSync.start();
                    }

                    // load a test if there is any
                    System.out.println("1");
                    InputStream configStream = ThreadManager.class.getResourceAsStream("/config/settings.json");
                    System.out.println("2");
                    if (configStream == null) {
                        try {
                            System.out.println("3");
                            throw new FileNotFoundException("Could not find settings.json inside the project resources!");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    Settings settings = mapper.readValue(configStream, Settings.class);
                    System.out.println("4");
                    File configDir = new File("config");
                    System.out.println("5");
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
                    Thread.sleep(30 * 60 * 1000);
                }
            } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
