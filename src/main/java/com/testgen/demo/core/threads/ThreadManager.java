package com.testgen.demo.core.threads;

import com.testgen.demo.Helper;
import com.testgen.demo.core.config.Settings;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Scanner;

public class ThreadManager implements Runnable{
    private Helper help = new Helper();
    @Override
    public void run() {
        try {
            File threadManagerLog = new File(help.getLogFile("threads"));
            if (!threadManagerLog.exists()) {
                threadManagerLog.createNewFile();
            }
            // time to decide what to do
            while (true) {
                // we will sync questions every 30 min
                Thread threadQuestionSync = new Thread(new QuestionSync());
                threadQuestionSync.setDaemon(true);
                threadQuestionSync.setName("QuestionSync");
                threadQuestionSync.start();

                // load a test if there is any
                InputStream configStream = ThreadManager.class.getResourceAsStream("/com/testgen/demo/config/settings.json");

                if (configStream == null) {
                    try {
                        throw new FileNotFoundException("Could not find settings.json inside the project resources!");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

                ObjectMapper mapper = new ObjectMapper();
                Settings settings = mapper.readValue(configStream, Settings.class);

                File configDir = new File("/com/testgen/demo/config");

                File [] files = configDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.contains("test");
                    }
                });

                int testFileCount = 0;
                for (File file : files) {
                    System.out.println(file);
                    testFileCount++;
                }
                if (settings.saveLastTests > testFileCount) {
                    // start
                    Thread testDownloadThread = new Thread( new LoadTests());
                    testDownloadThread.setDaemon(true);
                    testDownloadThread.setName("LoadTests");
                    testDownloadThread.start();
                }


                // wait 2 minutes to check again
                Thread.sleep(30 * 60 * 1000);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
