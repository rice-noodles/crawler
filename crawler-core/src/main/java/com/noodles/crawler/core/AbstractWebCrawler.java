package com.noodles.crawler.core;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.noodles.crawler.http.HttpHandler;
import com.noodles.crawler.property.CrawlerContext;
import com.noodles.crawler.property.CrawlerStatus;
import com.noodles.crawler.property.HttpInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: noodles
 * @date: 2021/12/04 9:39
 */
@Slf4j
public abstract class AbstractWebCrawler<T> implements WebCrawler {

    protected volatile CrawlerContext crawlerContext;

    @Setter
    @Getter
    private HttpHandler httpHandler;

    /*** 获取的数据结果 */
    private final Queue<T> dataQueue = new LinkedBlockingQueue<>();

    @Override
    public final synchronized void initialize() {
        if (this.crawlerContext == null) {
            throw new NullPointerException();
        }
        this.refreshStatus(CrawlerStatus.READY);
        log.info("crawler initial...");
    }

    @Override
    public final void run() throws Exception {
        if (crawlerContext == null) {
            log.error("The crawler is not initialize !");
            return;
        }
        this.beforeRun();
        if (isRunning()) {
            log.warn("[{}] crawler is running !", crawlerContext.getName());
            return;
        }
        refreshStatus(CrawlerStatus.RUNNING);
        if (!isRunning()) {
            throw new InterruptedException();
        }
        try {
            this.execute();
        } catch (Exception e) {
            log.error("[{}] crawler running error !", crawlerContext.getName(), e);
        }
    }

    @Override
    public final synchronized void finish() {
        if (CrawlerStatus.FINISHED.name().equals(this.crawlerContext.getStatus())) {
            return;
        }
        this.beforeFinish();
        this.refreshStatus(CrawlerStatus.FINISHED);
    }

    @Override
    public boolean isRunning() {
        if (this.crawlerContext == null) {
            return false;
        }
        return this.crawlerContext.getStatus().equals(CrawlerStatus.RUNNING.name());
    }

    @Override
    public void refreshStatus(CrawlerStatus status) {
        this.crawlerContext.setStatus(status.name());
        // todo refreshCrawler
    }

    @Override
    public HttpInfo getHttpInfo() {
        return this.crawlerContext.getHttpInfo();
    }

    @Override
    public CrawlerContext getCrawlerContext() {
        return this.crawlerContext;
    }

    @Override
    public void setCrawlerContext(CrawlerContext crawlerContext) {
        this.crawlerContext = crawlerContext;
    }

    protected boolean offer(T data) {
        if (isRunning()) {
            return this.dataQueue.offer(data);
        }
        return false;
    }

    protected T poll() {
        if (!this.dataQueue.isEmpty()) {
            return this.dataQueue.poll();
        }
        return null;
    }

    protected List<T> getList() {
        return new ArrayList<>(this.dataQueue);
    }

    protected int dataSize() {
        return this.dataQueue.size();
    }

    protected void beforeRun() {
    }

    protected void beforeFinish() {
    }

    protected HttpResponse doRequest(HttpRequest request) throws Exception {
        HttpInfo httpInfo = crawlerContext.getHttpInfo();
        String headers = httpInfo.getHeaders();
        String cookie = httpInfo.getCookie();
        int connectTimeOut = httpInfo.getConnectTimeOut();
        int readTimeOut = httpInfo.getReadTimeOut();
        request.addHeaders(JSONUtil.toBean(headers, new TypeReference<>() {
                }, false))
                .cookie(cookie)
                .setConnectionTimeout(connectTimeOut)
                .setReadTimeout(readTimeOut);
        /*request.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .header("X-CSRF-TOKEN", CRAWLER_CONTEXT.getCsrfToken())
                .cookie(CRAWLER_CONTEXT.getCookie())
                .setConnectionTimeout(5000)
                .setReadTimeout(60000);*/
        HttpResponse response = this.httpHandler.executeAndRetry(request, 3);
        if (response.getStatus() != HttpStatus.HTTP_OK) {
            log.error("request fail : {} {}", response.getStatus(), response.body());
        }
        return response;
    }

}
