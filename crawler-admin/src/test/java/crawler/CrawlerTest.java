package crawler;

import com.noodles.crawler.service.CrawlerInfoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Noodles
 * @date 2023/9/26 13:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerTest {

    @Autowired
    private CrawlerInfoService crawlerInfoService;

    @Test
    public void start() {
        crawlerInfoService.startCrawler(1L);
    }

}
