package com.plasticene.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 11:25
 */
@Data
public class SqlQueryQuery extends BaseQuery {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("分组id")
    private Long folderId;
}
