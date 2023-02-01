package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/31 18:28
 */
@Data
public class MenuDTO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单接口权限标识")
    private String code;

    @ApiModelProperty("父级菜单id")
    private Long parentId;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("组件")
    private String component;

    @ApiModelProperty("序号")
    private Integer seq;

    @ApiModelProperty("类型 0：菜单 1：按钮")
    private Integer type;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Long createTime;

}
