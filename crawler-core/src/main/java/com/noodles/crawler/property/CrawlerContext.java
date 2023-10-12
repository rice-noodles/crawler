package com.noodles.crawler.property;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrawlerContext {

    private Long id;

    private String name;

    private String status;

    private Integer instance;

    private Integer threadNum;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private HttpInfo httpInfo;

}
