package com.plasticene.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/2/2 15:31
 */
@Data
public class UserQuery extends BaseQuery{
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("状态 0：正常  -1：停用")
    private Integer status;
}
