package com.noodles.crawler.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Noodles
 * @since 2023-09-15
 */
@Getter
@Setter
@TableName("sys_request_info")
@ApiModel(value = "RequestInfo对象", description = "")
public class RequestInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("请求最大连接时间")
    private Integer connectTimeOut;

    @ApiModelProperty("请求最大读取时间")
    private Integer readTimeOut;

    @ApiModelProperty("cookie")
    private String cookie;

    @ApiModelProperty("请求参数")
    private String params;

    @ApiModelProperty("请求头")
    private String headers;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
