package com.noodles.crawler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.noodles.crawler.entity.CrawlerInfo;
import com.noodles.crawler.vo.page.CrawlerInfoPage;

/**
 * @author Noodles
 * @date 2023/9/12 10:11
 */
public interface CrawlerInfoService extends IService<CrawlerInfo> {

    void startCrawler(Long crawlerId);

    Page<CrawlerInfo> listPage(CrawlerInfoPage param);

}
