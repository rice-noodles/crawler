package com.noodles.crawler.crawler.property;

import com.noodles.crawler.entity.CrawlerInfo;
import com.noodles.crawler.entity.RequestInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class CrawlerContext implements Serializable {

    private static final long serialVersionUID = -8551342945509302912L;

    private CrawlerInfo crawlerInfo;

    private RequestInfo requestInfo;

    private Object data;

}
