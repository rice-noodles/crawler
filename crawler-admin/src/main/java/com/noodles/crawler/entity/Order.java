package com.noodles.crawler.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: noodles
 * @Date: 2022/03/06 11:48
 */
@Data
@Builder
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 3785948373930640645L;

    private String bizNo; // 唯一业务号(如id等)

    private String orderCode; // 订单号

    private String fullName; // 收货人姓名

    private String mobilePhone; // 收货人电话

    private String fullAddress; // 收货人详细地址

    private String province; // 省份

    private String city; // 城市

    private String area; // 地区

    private String address; // 城镇
}
