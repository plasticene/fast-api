package com.plasticene.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/6 14:06
 */
@Data
public class FolderQuery extends BaseQuery{
    @ApiModelProperty("分组名称")
    private String name;
}
