package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 10:35
 */
@Data
public class SqlQueryDTO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数据源id")
    private Long dataSourceId;
    @ApiModelProperty("数据源名称")
    private String dataSourceName;
    @ApiModelProperty("数据库名称")
    private String databaseName;
    @ApiModelProperty("SQL语句")
    private String sqlContent;
    @ApiModelProperty("SQL日志")
    private String sqlLog;
    @ApiModelProperty("分组id")
    private Long folderId;
    @ApiModelProperty("分组名称")
    private String folderName;
}
