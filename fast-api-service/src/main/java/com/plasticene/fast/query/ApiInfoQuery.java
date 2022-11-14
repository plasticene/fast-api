package com.plasticene.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 10:55
 */
@Data
public class ApiInfoQuery extends BaseQuery {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("分组")
    private Integer folderId;
    @ApiModelProperty("状态")
    private Integer status;
}
