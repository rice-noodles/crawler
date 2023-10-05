package com.noodles.crawler.vo.page;

import com.noodles.crawler.vo.PageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Noodles
 * @date 2023/9/14 16:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CrawlerInfoPage extends PageVo {

    private static final long serialVersionUID = 5251197914288740545L;

    private String name;

}
