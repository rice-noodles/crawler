package com.noodles.crawler.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noodles.crawler.CrawlerExecutor;
import com.noodles.crawler.core.WebCrawler;
import com.noodles.crawler.entity.CrawlerInfo;
import com.noodles.crawler.entity.RequestInfo;
import com.noodles.crawler.mapper.CrawlerInfoMapper;
import com.noodles.crawler.property.CrawlerContext;
import com.noodles.crawler.property.HttpInfo;
import com.noodles.crawler.service.CrawlerInfoService;
import com.noodles.crawler.service.RequestInfoService;
import com.noodles.crawler.vo.page.CrawlerInfoPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author Noodles
 * @date 2023/9/12 10:12
 */
@Service
public class CrawlerInfoServiceImpl extends ServiceImpl<CrawlerInfoMapper, CrawlerInfo> implements CrawlerInfoService, ApplicationContextAware {

    @Autowired
    private RequestInfoService requestInfoService;

    private ApplicationContext applicationContext;

    @Override
    public void startCrawler(Long crawlerId) {
        CrawlerInfo crawlerInfo = getById(crawlerId);
        if (crawlerInfo == null) {
            return;
        }
        RequestInfo requestInfo = requestInfoService.getById(crawlerInfo.getRequestInfoId());
        HttpInfo httpInfo = new HttpInfo();
        BeanUtils.copyProperties(requestInfo, httpInfo);

        CrawlerContext context = new CrawlerContext();
        BeanUtils.copyProperties(crawlerInfo, context);

        context.setHttpInfo(httpInfo);
        WebCrawler crawler = (WebCrawler) applicationContext.getBean(crawlerInfo.getServiceName());
        crawler.setCrawlerContext(context);

        CrawlerExecutor.start(crawler);
    }

    @Override
    public Page<CrawlerInfo> listPage(CrawlerInfoPage param) {
        LambdaQueryChainWrapper<CrawlerInfo> wrapper = lambdaQuery();
        if (StrUtil.isNotBlank(param.getName())) {
            wrapper.like(CrawlerInfo::getName, param.getName());
        }
        return page(param.getPage(), wrapper);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
