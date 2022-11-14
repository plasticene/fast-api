package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 10:59
 */
@Data
public class ApiReleaseDTO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("路径")
    private String path;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("数据源id")
    private Long dataSourceId;
    @ApiModelProperty("数据库名称")
    private String databaseName;
    @ApiModelProperty("SQL语句")
    private String sqlContent;
    @ApiModelProperty("参数")
    private List<Parameter> param;
    @ApiModelProperty("响应结果")
    private String response;
    @ApiModelProperty("api id")
    private Long apiInfoId;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
