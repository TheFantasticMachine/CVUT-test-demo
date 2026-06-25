package com.testgen.demo.core.threads;

import java.io.File;
import java.io.IOException;
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

            Scanner firstRead = new Scanner(threadManagerLog);

            // time to decide what to do
            while (true) {
                // we will sync questions every 30 min
                /// > find last time we synced

                // load a test if there is any

                // wait 2 minutes to check again
                Thread.sleep(120 * 1000);
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
