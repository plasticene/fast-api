package com.plasticene.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 10:37
 */
@Data
public class SqlQueryParam {
    @ApiModelProperty("名称")
    @NotBlank
    private String name;
    @ApiModelProperty("数据源id")
    @NotNull
    private Long dataSourceId;
    @ApiModelProperty("数据库名称")
    @NotBlank
    private String databaseName;
    @ApiModelProperty("分组id")
    @NotNull
    private Long folderId;
    @ApiModelProperty("SQL语句")
    private String sqlContent;

}
