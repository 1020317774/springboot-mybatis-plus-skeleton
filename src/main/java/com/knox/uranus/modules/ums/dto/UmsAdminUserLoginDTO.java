package com.knox.uranus.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录参数
 *
 * @author knox
 * @date 2020/08/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmsAdminUserLoginDTO {

    @NotEmpty(message = "账号为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotEmpty(message = "密码为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
