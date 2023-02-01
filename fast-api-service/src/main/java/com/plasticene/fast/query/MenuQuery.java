package com.plasticene.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/31 18:50
 */
@Data
public class MenuQuery {
    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单接口权限标识")
    private String code;

    @ApiModelProperty("类型 0：菜单 1：按钮")
    private Integer type;

    @ApiModelProperty("状态")
    private Integer status;
}
