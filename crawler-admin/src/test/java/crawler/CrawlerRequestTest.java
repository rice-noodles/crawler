package crawler;

import cn.hutool.http.HttpUtil;

/**
 * @author Noodles
 * @date 2023/9/28 14:21
 */
public class CrawlerRequestTest {

    private void startCrawler(Long crawlerId) {
        String url = "http://127.0.0.1:8080/crawler/start?id=1";
        String body = HttpUtil.get(url);
        System.out.println(body);
    }

    public static void main(String[] args) {
        CrawlerRequestTest test = new CrawlerRequestTest();
        test.startCrawler(1L);
    }

}
