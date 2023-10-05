package com.noodles.crawler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Noodles
 * @date 2023/9/28 15:04
 */
@MapperScan(basePackages = "com.noodles.crawler.mapper")
@SpringBootApplication
public class CrawlerAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerAdminApplication.class, args);
    }

}
