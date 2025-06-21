package com.spt.managesystem.common;

import lombok.Data;
/**
 * @author songpintong
 * 通用返回类
 */
@Data
public class BaseResponse<T> {
    /**
     * 状态码
     * 200 表示成功，其他为失败
     */
    private int code;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    /**
     * 描述
     */
    private String description;

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
