package com.noodles.crawler.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
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
@Schema(name = "RequestInfo对象", description = "")
public class RequestInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @SchemaProperty(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @SchemaProperty(name = "请求最大连接时间")
    private Integer connectTimeOut;

    @SchemaProperty(name = "请求最大读取时间")
    private Integer readTimeOut;

    @SchemaProperty(name = "cookie")
    private String cookie;

    @SchemaProperty(name = "请求参数")
    private String params;

    @SchemaProperty(name = "请求头")
    private String headers;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
