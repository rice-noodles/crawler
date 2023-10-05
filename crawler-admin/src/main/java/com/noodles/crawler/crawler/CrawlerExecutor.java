package com.noodles.crawler.crawler;

import com.noodles.crawler.crawler.factory.WebCrawler;
import com.noodles.crawler.crawler.property.CrawlerContext;
import com.noodles.crawler.crawler.property.CrawlerStatus;
import com.noodles.crawler.entity.CrawlerInfo;
import com.noodles.crawler.utils.ConcurrentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: noodles
 * @Date: 2022/03/24 11:00
 */
@Slf4j
@Component
public class CrawlerExecutor implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 启动一个爬虫，异步执行
     *
     * @param crawlerContext 爬虫任务
     */
    public static void start(CrawlerContext crawlerContext) {
        CrawlerInfo crawlerInfo = crawlerContext.getCrawlerInfo();
        WebCrawler crawler = (WebCrawler) context.getBean(crawlerInfo.getServiceName());
        crawler.initialize(crawlerContext);
        ConcurrentUtils.runParallel(crawlerInfo.getThreadNum(), () -> {
            try {
                crawler.run();
            } catch (Exception e) {
                log.error("[{}] crawler running error !", crawlerInfo.getName(), e);
            } finally {
                crawler.finish();
            }
        });
    }

    /**
     * 暂停一个爬虫
     *
     * @param crawlerContext 爬虫
     */
    public static void stop(CrawlerContext crawlerContext) {
        CrawlerInfo crawlerInfo = crawlerContext.getCrawlerInfo();
        WebCrawler crawler = (WebCrawler) context.getBean(crawlerInfo.getServiceName());
        if (!crawler.isRunning()) {
            return;
        }
        crawler.refreshCrawler(CrawlerStatus.STOP);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
