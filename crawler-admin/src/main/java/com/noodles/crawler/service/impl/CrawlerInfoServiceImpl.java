package com.noodles.crawler.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noodles.crawler.crawler.property.CrawlerContext;
import com.noodles.crawler.entity.CrawlerInfo;
import com.noodles.crawler.entity.RequestInfo;
import com.noodles.crawler.mapper.CrawlerInfoMapper;
import com.noodles.crawler.service.CrawlerInfoService;
import com.noodles.crawler.service.RequestInfoService;
import com.noodles.crawler.vo.page.CrawlerInfoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Noodles
 * @date 2023/9/12 10:12
 */
@Service
public class CrawlerInfoServiceImpl extends ServiceImpl<CrawlerInfoMapper, CrawlerInfo> implements CrawlerInfoService {

    @Autowired
    private RequestInfoService requestInfoService;

    @Override
    public void startCrawler(Long crawlerId) {
        CrawlerInfo crawlerInfo = getById(crawlerId);
        if (crawlerInfo == null) {
            return;
        }
        RequestInfo requestInfo = requestInfoService.getById(crawlerInfo.getRequestInfoId());

        CrawlerContext context = new CrawlerContext();
        context.setCrawlerInfo(crawlerInfo);
        context.setRequestInfo(requestInfo);
        // CrawlerExecutor.start(context);
    }

    @Override
    public Page<CrawlerInfo> listPage(CrawlerInfoPage param) {
        LambdaQueryChainWrapper<CrawlerInfo> wrapper = lambdaQuery();
        if (StrUtil.isNotBlank(param.getName())) {
            wrapper.like(CrawlerInfo::getName, param.getName());
        }
        return page(param.getPage(), wrapper);
    }

}
