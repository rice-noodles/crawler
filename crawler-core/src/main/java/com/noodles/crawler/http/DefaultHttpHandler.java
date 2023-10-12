package com.noodles.crawler.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 * @author Noodles
 * @date 2023/10/9 8:58
 */
public class DefaultHttpHandler implements HttpHandler {
    @Override
    public void successHandler(HttpRequest request, HttpResponse response, int retry) {

    }

    @Override
    public void failHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception {

    }

    @Override
    public void postExecutionHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception {

    }

}
