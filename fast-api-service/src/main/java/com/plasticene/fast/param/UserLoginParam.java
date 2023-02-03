package com.plasticene.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/10/31 11:35
 */

@Data
public class UserLoginParam {
    @ApiModelProperty("用户名(账户)")
    private String username;
    @ApiModelProperty("密码")
    private String password;
}
