package com.demo.util;

import org.apache.log4j.Logger;

public class CommonThread extends Thread {

    /**
     * Log Object
     */
    private static Logger logger = Logger.getLogger(CommonThread.class);
    /**
     * Mutex lock object
     */
    private static final Object mutex = new Object();
    /**
     * Number thread is running
     */
    private static int threadCount = 0;
    /**
     * Thread running flag
     * All thread must check this flag while running, if flag is false
     * thread must going to terminal status
     */
    private static boolean running = true;

    public CommonThread() {
    }

    /**
     * return thread running flag status
     * @return boolean
     */
    public static boolean isRunning() {
        synchronized (mutex) {
            return running;
        }

    }

    /**
     * Set running status
     * @params brunning type is boolean
     */
    public static void setRunningStatus(boolean brunning) {
        synchronized (mutex) {
            running = brunning;
        }

    }

    /**
     * Increase/Decrease number thread count.
     * @param val: use +1 value to increase thread count
     *             use -1 value to decrease thread count 
     */
    public static void adjustThreadCount(int val) {
        synchronized (mutex) {
            threadCount += val;
        }
    }

    /**
     * Return running thread count
     */
    public static int getRunningThreadCount() {
        synchronized (mutex) {
            return threadCount;
        }
    }

    /**
     * Sleep 
     * 
     */
    public static void sleep(long miliseconds) {

        while ((miliseconds > 0) && isRunning()) {
            try {
                if (miliseconds > 1000) {
                    Thread.sleep(1000);
                    miliseconds -= 1000;
                } else {
                    Thread.sleep(miliseconds);
                    miliseconds = 0;
                }

            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
}
