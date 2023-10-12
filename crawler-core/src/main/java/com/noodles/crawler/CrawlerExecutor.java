package com.noodles.crawler;

import com.noodles.crawler.core.Crawler;
import com.noodles.crawler.core.WebCrawler;
import com.noodles.crawler.property.CrawlerContext;
import com.noodles.crawler.utils.ConcurrentUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: noodles
 * @date: 2022/03/24 11:00
 */
@Slf4j
public class CrawlerExecutor {

    /**
     * 启动一个爬虫，异步执行
     *
     * @param crawler 爬虫
     */
    public static void start(WebCrawler crawler) {
        crawler.initialize();
        CrawlerContext context = crawler.getCrawlerContext();
        ConcurrentUtils.runParallel(context.getThreadNum(), () -> {
            try {
                crawler.run();
            } catch (Exception e) {
                log.error("[{}] crawler running error !", context.getName(), e);
            } finally {
                crawler.finish();
            }
        });
    }

    /**
     * 暂停一个爬虫
     *
     * @param crawler 爬虫
     */
    public static void stop(Crawler crawler) {
        crawler.finish();
    }

}
