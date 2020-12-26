package com.knox.uranus.common.api;

/**
 * 枚举了一些常用API操作码
 * <p>
 * 可与前端对接，完善业务代码
 *
 * @author knox
 * @date 2020/08/19
 */
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "操作成功"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    VALIDATE_FAILED(404, "参数检验失败"),
    FAILED(500, "操作失败");

    private Long code;
    private String message;

    /**
     * 构造
     *
     * @param code
     * @param message
     */
    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
