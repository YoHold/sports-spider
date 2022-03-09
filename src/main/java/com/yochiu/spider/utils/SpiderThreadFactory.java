package com.yochiu.spider.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class SpiderThreadFactory implements ThreadFactory {

    private final String namePrefix;

    private final boolean daemon;

    private final AtomicLong threadNumber = new AtomicLong(1);

    private static final ThreadGroup threadGroup = new ThreadGroup("Alert");

    public static SpiderThreadFactory create(String namePrefix, boolean daemon) {
        return new SpiderThreadFactory(namePrefix, daemon);
    }

    private SpiderThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }


    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(threadGroup, runnable, threadGroup.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
