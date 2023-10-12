package com.noodles.crawler.crawler;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.noodles.crawler.core.AbstractWebCrawler;
import com.noodles.crawler.entity.BaiduLink;
import com.noodles.crawler.property.HttpInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Noodles
 * @date 2023/9/13 16:02
 */
@Slf4j
@Service("baiduLinkCrawler")
public class BaiduLinkCrawler extends AbstractWebCrawler<BaiduLink> {

    private final static String HOST = "https://www.baidu.com/s?wd=%s&pn=%s";

    private final AtomicInteger pn = new AtomicInteger(0);

    @Override
    public void execute() throws Exception {
        HttpInfo httpInfo = getHttpInfo();

        Params params = new Params(httpInfo.getParams());
        synchronized (this) {
            if (this.pn.get() == 0) {
                String pnValue = params.getPnValue();
                this.pn.set(pnValue == null ? 0 : Integer.parseInt(pnValue));
            }
        }
        this.linkCrawler("测试");
    }

    private void linkCrawler(String wd) {
        wd = URLEncoder.createDefault().encode(wd, StandardCharsets.UTF_8);
        String url = String.format(HOST, wd, pn.getAndAdd(10));
        HttpRequest request = HttpRequest.get(url);
        String html;
        try {
            HttpResponse response = doRequest(request);
            html = response.body();
        } catch (Exception e) {
            log.error("请求出错 : {}", url, e);
            return;
        }
        if (html == null) {
            log.error("请求获取页面失败 : {}", url);
            return;
        }
        Document doc = Jsoup.parse(html);
        Element root = doc.body();
        Element leftContent = root.getElementById("content_left");
        Elements titleList = leftContent.getElementsByTag("a");
        for (Element a : titleList) {
            String href = a.attr("href");
            String title = a.text();

            BaiduLink entity = new BaiduLink();
            entity.setUrl(href);
            entity.setTitle(title);
            this.offer(entity);
        }
    }

    public static class Params {

        public static String pn = "pn";

        private final JSONObject params;

        Params() {
            params = new JSONObject();
            params.set(pn, "0");
        }

        Params(String params) {
            this.params = JSONUtil.parseObj(params);
        }

        public String jsonString() {
            return params.toString();
        }

        public String getPnValue() {
            return (String) params.get(pn);
        }

    }

}
