package com.noodles.crawler.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Noodles
 * @date 2023/9/12 9:33
 */
@Data
public class CrawlerInfo implements Serializable {

    private static final long serialVersionUID = 9176482950272044968L;

    private Long id;

    private Long requestInfoId;

    private String name;

    private String serviceName;

    private String status;

    private Integer instance;

    private Integer threadNum;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
