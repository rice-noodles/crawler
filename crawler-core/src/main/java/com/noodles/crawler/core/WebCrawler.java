package com.noodles.crawler.core;

import com.noodles.crawler.property.CrawlerContext;
import com.noodles.crawler.property.CrawlerStatus;
import com.noodles.crawler.property.HttpInfo;

/**
 * @author Noodles
 * @since 2023/9/13 14:30
 */
public interface WebCrawler extends Crawler {

    void execute() throws Exception;

    boolean isRunning();

    void refreshStatus(CrawlerStatus status);

    HttpInfo getHttpInfo();

    CrawlerContext getCrawlerContext();

    void setCrawlerContext(CrawlerContext crawlerContext);

}
