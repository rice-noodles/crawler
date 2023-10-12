package com.noodles.crawler.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Noodles
 * @date 2023/9/13 15:45
 */
@Data
public class BaiduLink implements Serializable {

    @Serial
    private static final long serialVersionUID = 8431084285472455374L;

    private Long id;

    private String url;

    private String title;

}
