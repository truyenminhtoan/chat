package com.demo.util;

/**
 *
 * @author toantm1
 */
public interface ThreadProcess<T> {
    /**
     * Process an elements in queue
     */
    void process(T obj);
}
