package com.noodles.crawler.property;

import com.noodles.crawler.http.HttpHandler;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Noodles
 * @since 2023-09-15
 */
@Data
public class HttpInfo {

    private Integer connectTimeOut;

    private Integer readTimeOut;

    private String cookie;

    private String params;

    private String headers;

    private HttpHandler httpHandler;

}
