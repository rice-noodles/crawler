package com.noodles.crawler.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.noodles.crawler.http.impl.DefaultHttpHandler;
import org.springframework.http.HttpStatus;

/**
 * @author Noodles
 * @date 2023/9/4 9:49
 */
public interface HttpHandler {

    /**
     * 请求成功时做出的处理
     *
     * @param request  HttpRequest
     * @param response HttpResponse
     * @param retry    重试次数
     * @return HttpStatus
     */
    HttpStatus successHandler(HttpRequest request, HttpResponse response, int retry);

    /**
     * 请求失败时做出的处理
     *
     * @param request  HttpRequest
     * @param response HttpResponse
     * @param e        捕获的异常
     * @param retry    重试次数
     * @return HttpStatus
     */
    HttpStatus failHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception;

    /**
     * 请求完后执行的处理
     *
     * @param request  HttpRequest
     * @param response HttpResponse
     * @param e        捕获的异常
     * @param retry    重试次数
     * @return HttpStatus
     */
    HttpStatus postExecutionHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception;

    /**
     * @param request     请求体
     * @param maxRetry    最大重试次数
     * @param httpHandler 请求成功、失败、完成时的处理
     * @return HttpResponse
     * @throws Exception
     */
    static HttpResponse executeAndRetry(HttpRequest request, int maxRetry, HttpHandler httpHandler) throws Exception {
        if (maxRetry < 1) {
            maxRetry = 1;
        }
        if (httpHandler == null) {
            httpHandler = new DefaultHttpHandler();
        }
        HttpResponse response = null;
        Exception exception = null;
        for (int retry = 1; retry <= maxRetry; retry++) {
            // The interrupted status of the thread will be cleared.
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            try {
                response = request.execute();
            } catch (Exception e) {
                exception = e;
                httpHandler.failHandler(request, response, exception, retry);
            } finally {
                httpHandler.postExecutionHandler(request, response, exception, retry);
            }
            HttpStatus httpStatus = httpHandler.successHandler(request, response, retry);
            if (httpStatus == null || httpStatus.is2xxSuccessful()) {
                return response;
            }
        }
        return response;
    }
}

