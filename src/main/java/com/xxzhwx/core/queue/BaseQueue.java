package com.xxzhwx.core.queue;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BaseQueue<T> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    /**
     * @see Queue#poll()
     */
    public T poll() {
        return queue.poll();
    }

    /**
     * @see BlockingQueue#put(Object)
     */
    public void put(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see Collection#size()
     */
    public int size() {
        return queue.size();
    }
}
