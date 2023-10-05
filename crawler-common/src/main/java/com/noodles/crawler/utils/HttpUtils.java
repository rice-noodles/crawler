package com.noodles.crawler.utils;

import cn.hutool.http.HttpResponse;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: noodles
 * @date: 2021/12/04 9:41
 */
public class HttpUtils {

    /**
     * @param response HttpResponse
     * @return response中的所有cookie
     */
    public static String getCookieStr(HttpResponse response) {
        List<HttpCookie> cookies = response.getCookies();
        HttpCookie[] httpCookies = cookies.toArray(new HttpCookie[0]);
        return getCookieStr(httpCookies);
    }

    /**
     * @param cookies HttpCookie数组
     * @return HttpCookie[]拼接成的字符串
     */
    public static String getCookieStr(HttpCookie[] cookies) {
        if (cookies != null && cookies.length > 0) {
            StringBuilder builder = new StringBuilder();
            //拼接成字符串
            for (HttpCookie cookie : cookies) {
                builder.append(cookie.getName())
                        .append("=")
                        .append(cookie.getValue())
                        .append(";");
            }
            //去掉最后的；
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }
        return "";
    }

    /**
     * @param cookies cookiesMap
     * @return map拼接成的字符串
     */
    public static String concatCookie(Map<String, String> cookies) {
        if (cookies != null && cookies.size() > 0) {
            StringBuilder builder = new StringBuilder();
            //拼接成字符串
            for (Map.Entry<String, String> httpCookie : cookies.entrySet()) {
                builder.append(httpCookie.getKey())
                        .append("=")
                        .append(httpCookie.getValue())
                        .append(";");
            }
            //去掉最后的；
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }
        return "";
    }

    /**
     * @param cookie cookie字符串
     * @return 把cookie转换成key-value的map格式，如果没有则返回空map
     */
    public static Map<String, String> cookieMap(String cookie) {
        Map<String, String> map = new HashMap<>();
        if (cookie == null || cookie.trim().isEmpty()) {
            return null;
        }
        String[] strings = cookie.split(";");
        for (String s : strings) {
            String[] split = s.split("=");
            if (split.length < 2) {
                continue;
            }
            map.put(split[0], split[1]);
        }
        return map;
    }

}
