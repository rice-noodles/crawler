package com.noodles.crawler.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;

/**
 * @author Noodles
 * @date 2023/10/9 8:57
 */
public interface HttpHandler {

    /**
     * 请求成功时做出的处理
     *
     * @param request  HttpRequest
     * @param response HttpResponse
     * @param retry    重试次数
     */
    void successHandler(HttpRequest request, HttpResponse response, int retry);

    /**
     * 请求失败时做出的处理
     *
     * @param request  HttpRequest
     * @param response HttpResponse
     * @param e        捕获的异常
     * @param retry    重试次数
     */
    void failHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception;

    /**
     * 请求完后执行的处理
     *
     * @param request  HttpRequest
     * @param response HttpResponse
     * @param e        捕获的异常
     * @param retry    重试次数
     */
    void postExecutionHandler(HttpRequest request, HttpResponse response, Exception e, int retry) throws Exception;

    /**
     * @param request  请求体
     * @param maxRetry 最大重试次数
     * @return HttpResponse
     */
    default HttpResponse executeAndRetry(HttpRequest request, int maxRetry) throws Exception {
        if (maxRetry < 1) {
            maxRetry = 1;
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
                this.failHandler(request, response, exception, retry);
            } finally {
                this.postExecutionHandler(request, response, exception, retry);
            }
            if (response != null && HttpStatus.HTTP_OK == response.getStatus()) {
                this.successHandler(request, response, retry);
                return response;
            }
        }
        return response;
    }

}
