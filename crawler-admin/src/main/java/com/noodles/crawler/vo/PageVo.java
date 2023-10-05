package com.noodles.crawler.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.noodles.crawler.exception.CustomException;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页查询参数
 *
 * @author Noodles
 * @date 2022/11/16 13:40
 */
@Data
public class PageVo implements Serializable {
    private static final long serialVersionUID = 4500173212834020719L;

    private Long current = 1L;

    private Long size = 10L;

    private List<String> sortFields = Collections.emptyList();

    private Boolean asc = false;

    public void setSortFields(List<String> sortFields) {
        for (String field : sortFields) {
            if (SqlInjectionUtils.check(field)) {
                throw new CustomException();
            }
        }
        this.sortFields = sortFields;
    }

    public <T> Page<T> getPage() {
        return Page.of(this.getCurrent(), this.getSize());
    }

    /**
     * 根据排序字段排序
     *
     * @param wrapper QueryWrapper
     */
    public void orderBy(QueryWrapper<?> wrapper) {
        List<String> orderByColumns = new ArrayList<>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(wrapper.getEntityClass());
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (this.sortFields.contains(fieldInfo.getProperty())) {
                orderByColumns.add(fieldInfo.getColumn());
            }
        }
        if (orderByColumns.isEmpty()) {
            orderByColumns.add(tableInfo.getKeyColumn());
        }
        wrapper.orderBy(true, this.asc, orderByColumns);
    }

    /**
     * 将排序字段转成数据表字段
     */
    public void convertToColumnFields(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (this.sortFields.contains(tableInfo.getKeyProperty())) {
            list.add(tableInfo.getKeyColumn());
        }
        Set<String> fieldNames = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName).collect(Collectors.toSet());

        Map<String, TableFieldInfo> tableInfos = tableInfo.getFieldList().stream()
                .collect(Collectors.toMap(TableFieldInfo::getProperty, Function.identity()));

        for (String field : this.sortFields) {
            if (tableInfos.containsKey(field)) {
                list.add(tableInfos.get(field).getColumn());
            } else if (fieldNames.contains(field)) {
                list.add(field);
            }
        }
        setSortFields(list);
    }

}
