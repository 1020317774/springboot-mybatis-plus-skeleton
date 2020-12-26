package com.knox.uranus.common.api;

/**
 * 封装API的错误码接口
 *
 * @author knox
 * @date 2020/08/19
 */
public interface IErrorCode {
    /**
     * 获取状态码
     *
     * @return 状态码
     */
    long getCode();

    /**
     * 获取声明
     *
     * @return 说明
     */
    String getMessage();
}
