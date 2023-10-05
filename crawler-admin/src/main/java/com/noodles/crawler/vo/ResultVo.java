package com.noodles.crawler.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Noodles
 * @date 2023/9/4 9:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = -524004876904451507L;

    private int code;

    private String message;

    private T data;

    private static <T> ResultVo<T> result(int code, String message, T data) {
        return new ResultVo<>(code, message, data);
    }

    public static <T> ResultVo<T> ok(T data) {
        return result(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data);
    }

    public static <T> ResultVo<T> ok() {
        return ok(null);
    }

    public static <T> ResultVo<T> fail(String msg) {
        return result(Constant.ERROR_CODE, msg, null);
    }

    public static <T> ResultVo<T> fail() {
        return fail(Constant.ERROR_MSG);
    }

    public static class Constant {
        public static final int SUCCESS_CODE = 1;

        public static final int ERROR_CODE = 0;

        public static final String SUCCESS_MSG = "OK";

        public static final String ERROR_MSG = "ERROR";
    }

}

