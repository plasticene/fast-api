package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/29 15:46
 */
@Data
public class RoleDTO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("是否系统内置 0：否  1：是")
    private Integer isSystem;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("创建人")
    private Long creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
}
