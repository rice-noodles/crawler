package com.noodles.crawler.crawler.factory;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.noodles.crawler.crawler.property.CrawlerContext;
import com.noodles.crawler.crawler.property.CrawlerStatus;
import com.noodles.crawler.entity.CrawlerInfo;
import com.noodles.crawler.entity.RequestInfo;
import com.noodles.crawler.http.HttpHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: noodles
 * @Date: 2021/12/04 9:39
 */

@Slf4j
public abstract class AbstractWebCrawler<T> implements WebCrawler {

    protected volatile CrawlerContext crawlerContext;

    private volatile CrawlerInfo crawlerInfo;

    /*** 获取的数据结果 */
    private final Queue<T> dataQueue = new LinkedBlockingQueue<>();

    @Override
    public final synchronized void initialize(CrawlerContext context) {
        if (crawlerContext == null) {
            this.crawlerContext = context;
            this.crawlerInfo = context.getCrawlerInfo();
            this.refreshCrawler(CrawlerStatus.READY);
            log.info("crawler initial...");
        }
    }

    @Override
    public final void run() throws Exception {
        if (crawlerContext == null) {
            log.error("The crawler is not initialize !");
            return;
        }
        this.beforeRun();
        if (isRunning()) {
            log.warn("[{}] crawler is running !", crawlerInfo.getName());
            return;
        }
        refreshCrawler(CrawlerStatus.RUNNING);
        if (!isRunning()) {
            throw new InterruptedException();
        }
        try {
            this.execute();
        } catch (Exception e) {
            log.error("[{}] crawler running error !", crawlerInfo.getName(), e);
        }
    }

    @Override
    public final synchronized void finish() {
        if (CrawlerStatus.FINISHED.name().equals(this.crawlerInfo.getStatus())) {
            return;
        }
        this.beforeFinish();
        this.refreshCrawler(CrawlerStatus.FINISHED);
    }

    @Override
    public boolean isRunning() {
        if (this.crawlerInfo == null) {
            return false;
        }
        return this.crawlerInfo.getStatus().equals(CrawlerStatus.RUNNING.name());
    }

    @Override
    public void refreshCrawler(CrawlerStatus status) {
        this.crawlerInfo.setStatus(status.name());
        // todo refreshCrawler
    }

    @Override
    public int dataSize() {
        return this.dataQueue.size();
    }

    @Override
    public RequestInfo getRequestInfo() {
        if (this.crawlerContext == null) {
            return null;
        }
        return this.crawlerContext.getRequestInfo();
    }

    protected boolean addData(T data) {
        if (isRunning()) {
            return this.dataQueue.offer(data);
        }
        return false;
    }

    protected void beforeRun() {
    }

    protected void beforeFinish() {
    }

    protected HttpResponse doRequest(HttpRequest request) throws Exception {
        RequestInfo requestInfo = crawlerContext.getRequestInfo();
        String headers = requestInfo.getHeaders();
        String cookie = requestInfo.getCookie();
        int connectTimeOut = requestInfo.getConnectTimeOut();
        int readTimeOut = requestInfo.getReadTimeOut();
        request.addHeaders(JSONUtil.toBean(headers, new TypeReference<Map<String, String>>() {
                }, false))
                .cookie(cookie)
                .setConnectionTimeout(connectTimeOut)
                .setReadTimeout(readTimeOut);
        /*request.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .header("X-CSRF-TOKEN", CRAWLER_CONTEXT.getCsrfToken())
                .cookie(CRAWLER_CONTEXT.getCookie())
                .setConnectionTimeout(5000)
                .setReadTimeout(60000);*/
        HttpResponse response = HttpHandler.executeAndRetry(request, 3, null);
        if (response.getStatus() != HttpStatus.HTTP_OK) {
            log.error("request fail : {} {}", response.getStatus(), response.body());
        }
        return response;
    }

}
