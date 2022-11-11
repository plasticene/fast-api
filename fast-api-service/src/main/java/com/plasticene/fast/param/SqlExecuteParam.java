package com.plasticene.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/8 11:40
 */
@Data
public class SqlExecuteParam {
    @ApiModelProperty("数据源id")
    @NotNull
    private Long dataSourceId;
    @ApiModelProperty("数据库名称")
    @NotBlank
    private String databaseName;
    @ApiModelProperty("SQL语句")
    @NotBlank
    private String sqlContent;

    @ApiModelProperty("分页大小")
    private Integer pageSize = 20;
    @ApiModelProperty("页码")
    private Integer pageNo = 1;
}
