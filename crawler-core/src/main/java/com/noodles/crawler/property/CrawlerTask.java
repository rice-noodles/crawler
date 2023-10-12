package com.noodles.crawler.property;

import lombok.Data;

import java.util.concurrent.FutureTask;

/**
 * @author Noodles
 * @date 2023/9/5 14:05
 */
@Deprecated
@Data
public class CrawlerTask<T> {

    private CrawlerContext context;

    private Runnable task;

    private int threadNum;

    private T result;

/*    public CrawlerTask(CrawlerInfo crawlerInfo, Callable<T> task, int threadNum) {
        this.crawlerInfo = crawlerInfo;
        this.task = new FutureTask<>(task);
        this.threadNum = threadNum;
    }*/

    public CrawlerTask(CrawlerContext context, Runnable task, int threadNum) {
        this.context = context;
        this.task = new FutureTask<>(task, null);
        this.threadNum = threadNum;
    }

}
