package com.noodles.crawler.crawler.factory;

import com.noodles.crawler.crawler.property.CrawlerStatus;
import com.noodles.crawler.entity.RequestInfo;

/**
 * @author Noodles
 * @since 2023/9/13 14:30
 */
public interface WebCrawler extends Crawler {

    void execute() throws Exception;

    boolean isRunning();

    void refreshCrawler(CrawlerStatus status);

    int dataSize();

    RequestInfo getRequestInfo();

}
