package com.testgen.demo.core.threads;

import com.testgen.demo.Globals;
import com.testgen.demo.Helper;
import com.testgen.demo.core.config.FileHandler;
import com.testgen.demo.core.config.Settings;
import tools.jackson.databind.ObjectMapper;

import java.io.*;

public class ThreadManager implements Runnable{
    private Helper help = new Helper();
    private FileHandler fileHandler = new FileHandler();
    @Override
    public void run() {
        try {
            // start log
            String logPath = fileHandler.getLogFile("threads");
            fileHandler.createFile(new String[] {"logs/threads.txt"});
            File log = new File(logPath);
            Globals globals = new Globals();
            globals.setTheadLog(log);

            // start first sync
            fileHandler.write(log, "started question sync");
            Thread threadQuestionSync = new Thread(new QuestionSync());
            threadQuestionSync.setName("QuestionSync");
            threadQuestionSync.start();

                // time to decide what to do
                while (true) {
                    // we will sync questions every 30 min
                    threadQuestionSync.start();

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
