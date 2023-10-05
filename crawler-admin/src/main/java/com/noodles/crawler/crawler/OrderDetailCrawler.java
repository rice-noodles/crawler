package com.noodles.crawler.crawler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noodles.crawler.crawler.factory.AbstractWebCrawler;
import com.noodles.crawler.entity.Order;
import com.noodles.crawler.utils.MyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: noodles
 * @Date: 2022/03/02 10:43
 */
@Slf4j
@Component(value = "OrderDetailCrawler")
public class OrderDetailCrawler extends AbstractWebCrawler<Order> {

    private static final String LIST_URL = "https://tgc.tmall.com/api/v1/orderNew/getTradeOrders.htm?pageNo={0}&pageSize={1}&sourceTradeId=&status=PAID";

    private static final String DETAIL_URL = "https://tgc.tmall.com/ds/api/v1/o/qOsi";

    // 是否超过请求上限
    private final static AtomicBoolean OVER = new AtomicBoolean(false);

    private final AtomicInteger totalCount = new AtomicInteger(200);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute() {
        Queue<Order> orders = this.dealList();
        this.dealDetail(orders);
    }

    @Override
    protected void beforeFinish() {
        Object data = this.crawlerContext.getData();
        this.exportToExcel((Iterable<?>) data);
    }

    private Queue<Order> dealList() {
        Queue<Order> queue = new LinkedBlockingQueue<>();
        HttpRequest request = HttpRequest.get(MessageFormat.format(LIST_URL, 1, totalCount.get()));
        HttpResponse response = null;
        try {
            response = doRequest(request);
            String body = response.body();
            JsonNode root = objectMapper.readTree(body);
            for (JsonNode node : root.get("data")) {
                String orderCode = node.get("sourceTradeId").asText();
                Order order = Order.builder().orderCode(orderCode).build();
                queue.offer(order);
            }
        } catch (Exception e) {
            Integer status = response == null ? null : response.getStatus();
            log.error("获取列表失败 : " + status, e);
        }
        return queue;
    }

    private void dealDetail(Queue<Order> queue) {
        while (!queue.isEmpty()) {
            Order order = queue.poll();
            Map<String, Object> form = new HashMap<>();
            form.put("mainOrderId", order.getOrderCode());
            form.put("infoKeys", Arrays.asList("fullName", "mobilephone", "fullAddress"));
            HttpRequest request = HttpRequest.post(DETAIL_URL).body(JSONUtil.toJsonStr(form));
            HttpResponse response = null;
            try {
                response = doRequest(request);
                if (response != null && !OVER.get()) {
                    String body = response.body();
                    // 请求达到当日上限
                    if (StrUtil.contains(body, "QUERY_SENSITIVE_INFO_MAX_LIMIT")) {
                        if (!OVER.get()) {
                            log.info("请求次数已达当日上限。其余数据已经暂存。");
                        }
                        OVER.set(true);
                        return;
                    }
                    JsonNode root = objectMapper.readTree(body);
                    JsonNode node = root.get("data");

                    String province = node.get("prov").asText();
                    String city = node.get("city").asText();
                    String area = node.get("area").asText();
                    String address = node.get("address").asText();
                    String mobilePhone = node.get("mobilephone").asText();
                    String fullAddress = node.get("fullAddress").asText();
                    String fullName = node.get("fullName").asText();

                    order.setFullName(fullName);
                    order.setMobilePhone(mobilePhone);
                    order.setFullAddress(fullAddress);
                    order.setProvince(province);
                    order.setCity(city);
                    order.setArea(area);
                    order.setAddress(address);
                    if (!this.addData(order)) {
                        log.error("数据添加失败");
                    }

                    if (dataSize() % 10 == 0) {
                        log.info("当前已获取" + dataSize() + "条数据...");
                        Thread.sleep(500);
                    }
                }
            } catch (Exception e) {
                Integer status = response == null ? null : response.getStatus();
                log.error("获取详情失败：" + status, e);
            }
        }
    }

    private void exportToExcel(Iterable<?> list) {
        if (list == null || !list.iterator().hasNext()) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("订单号", "订单号");
        writer.addHeaderAlias("收货人姓名", "收货人姓名");
        writer.addHeaderAlias("收货人电话", "收货人电话");
        writer.addHeaderAlias("收货地址", "收货地址");
        writer.addHeaderAlias("省份", "省份");
        writer.addHeaderAlias("城市", "城市");
        writer.addHeaderAlias("地区", "地区");
        writer.addHeaderAlias("城镇", "城镇");
        writer.write(list, true);
        MyExcelUtils.setSizeColumn(writer.getSheet(), writer.getColumnCount());
        File file = FileUtil.file(System.getProperty("user.dir"), "deliveryInfo.xlsx");
        writer.flush(file);
        writer.close();

        log.info("请查看文件：" + System.getProperty("user.dir") + "\\orderInfo.xlsx");
    }

}
