package com.noodles.crawler.core;


/**
 * @author: noodles
 * @date: 2021/12/04 9:37
 */
public interface Crawler {

    void initialize();

    void run() throws Exception;

    void finish();

}
