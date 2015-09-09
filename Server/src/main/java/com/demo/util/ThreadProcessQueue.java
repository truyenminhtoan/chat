package com.demo.util;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class ThreadProcessQueue extends CommonThread {
    /*
     * Queue to process
     */

    private BlockingQueue queue = null;
    /**
     * Queue processor
     */
    private ThreadProcess processor;
    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(ThreadProcessQueue.class.getClass());

    /**
     * Creates a new instance of ThreadProcessQueue
     *
     */
    public ThreadProcessQueue(BlockingQueue queue, ThreadProcess processor) {
        this.queue = queue;
        this.processor = processor;
    }

    @Override
    public void run() {
        adjustThreadCount(+1);
        while (CommonThread.isRunning()) {
            try {
                while (queue.size() > 0) {
                    Object obj = queue.take();
                    if (obj != null) {
                        processor.process(obj);
                    }
                }

                /**
                 * Wait until at least element is inserted into queue
                 */
                synchronized (queue) {
                    queue.wait(2000);
                }
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
            }
        }
        adjustThreadCount(-1);
    }
}
