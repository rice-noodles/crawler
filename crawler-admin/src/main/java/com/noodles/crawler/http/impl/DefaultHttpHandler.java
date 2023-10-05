package com.noodles.crawler.http.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.noodles.crawler.http.HttpHandler;
import org.springframework.http.HttpStatus;

/**
 * @author Noodles
 * @date 2023/9/4 9:49
 */
public class DefaultHttpHandler implements HttpHandler {
    @Override
    public HttpStatus successHandler(HttpRequest request, HttpResponse response, int retry) {
        return response == null ? null : HttpStatus.valueOf(response.getStatus());
    }

    @Override
    public HttpStatus failHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception {
        if (e != null) {
            throw e;
        }
        return response == null ? null : HttpStatus.valueOf(response.getStatus());
    }

    @Override
    public HttpStatus postExecutionHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception {
        if (e != null) {
            throw e;
        }
        return response == null ? null : HttpStatus.valueOf(response.getStatus());
    }
}
