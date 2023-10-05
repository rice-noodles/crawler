package com.noodles.crawler.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询统一响应数据格式
 *
 * @author Noodles
 * @date 2022/11/16 13:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultVo<T> extends ResultVo<T> {

    private static final long serialVersionUID = -481400877427412859L;

    private long current;

    private long size;

    private long total;

    public static <T> PageResultVo<List<T>> ok(IPage<T> page) {
        ResultVo<List<T>> ok = ResultVo.ok(page.getRecords());
        PageResultVo<List<T>> result = new PageResultVo<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setData(ok.getData());
        result.setCode(ok.getCode());
        result.setMessage(ok.getMessage());
        return result;
    }

}
