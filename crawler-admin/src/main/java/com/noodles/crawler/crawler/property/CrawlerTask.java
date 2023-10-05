package com.noodles.crawler.crawler.property;

import com.noodles.crawler.entity.CrawlerInfo;
import lombok.Data;

import java.util.concurrent.FutureTask;

/**
 * @author Noodles
 * @date 2023/9/5 14:05
 */
@Deprecated
@Data
public class CrawlerTask<T> {

    private CrawlerInfo crawlerInfo;

    private Runnable task;

    private int threadNum;

    private T result;

/*    public CrawlerTask(CrawlerInfo crawlerInfo, Callable<T> task, int threadNum) {
        this.crawlerInfo = crawlerInfo;
        this.task = new FutureTask<>(task);
        this.threadNum = threadNum;
    }*/

    public CrawlerTask(CrawlerInfo crawlerInfo, Runnable task, int threadNum) {
        this.crawlerInfo = crawlerInfo;
        this.task = new FutureTask<>(task, null);
        this.threadNum = threadNum;
    }

}
