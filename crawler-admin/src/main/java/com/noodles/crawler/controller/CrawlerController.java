package com.noodles.crawler.controller;

import com.noodles.crawler.service.CrawlerInfoService;
import com.noodles.crawler.vo.ResultVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Noodles
 * @date 2023/9/28 14:23
 */
@Api(tags = "爬虫控制")
@RequestMapping("/crawler")
@RestController
public class CrawlerController {

    @Autowired
    private CrawlerInfoService crawlerInfoService;

    @GetMapping("/start")
    public ResultVo<?> start(@RequestParam("id") long id) {
        crawlerInfoService.startCrawler(id);
        return ResultVo.ok();
    }

}
