package com.noodles.crawler.crawler.factory;


import com.noodles.crawler.crawler.property.CrawlerContext;

/**
 * @Author: noodles
 * @Date: 2021/12/04 9:37
 */
public interface Crawler {

    void initialize(CrawlerContext crawlerContext);

    void run() throws Exception;

    void finish();
}
