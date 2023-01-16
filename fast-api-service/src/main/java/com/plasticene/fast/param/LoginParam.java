package com.plasticene.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/3 17:24
 */
@Data
public class LoginParam {
    @ApiModelProperty(value = "账号", required = true, example = "shepherd")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "pass123456")
    private String password;

    @ApiModelProperty("登录方式 0：用户名  1：手机号  2：扫码")
    private Integer type;

    @ApiModelProperty("手机号")
    public String mobile;

    @ApiModelProperty("验证码")
    public String code;


}
