package com.plasticene.fast.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 16:59
 */
@Data
public class LoginVO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别 0：男  1：女")
    private Integer gender;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("地址")
    private String address;
}
