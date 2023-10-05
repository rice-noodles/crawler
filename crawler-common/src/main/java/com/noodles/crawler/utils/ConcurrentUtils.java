package com.noodles.crawler.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: noodles
 * @date: 2021/12/04 12:09
 */
public class ConcurrentUtils {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    /**
     * 并发执行
     *
     * @param threadNum
     * @param task
     */
    public static void runParallel(int threadNum, Runnable task) {
        for (int k = 0; k < threadNum; k++) {
            EXECUTOR_SERVICE.submit(task);
        }
    }

    /**
     * 异步执行，无返回值
     *
     * @param task
     */
    public static void runAsynchronous(Runnable task) {
        EXECUTOR_SERVICE.submit(task);
    }
}
