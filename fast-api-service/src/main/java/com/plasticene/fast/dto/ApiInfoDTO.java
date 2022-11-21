package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 10:52
 */
@Data
public class ApiInfoDTO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("路径")
    private String path;
    @ApiModelProperty("类型 0：get  1：post")
    private Integer type;
    @ApiModelProperty("数据源id")
    private Long dataSourceId;
    @ApiModelProperty("数据源名称")
    private String dataSourceName;
    @ApiModelProperty("数据库名称")
    private String databaseName;
    @ApiModelProperty("SQL语句")
    private String sqlContent;
    @ApiModelProperty("接口参数")
    private List<Parameter> param;
    @ApiModelProperty("响应结果")
    private String response;
    @ApiModelProperty("发布时间")
    private Date releaseTime;
    @ApiModelProperty("状态：0:草稿 1：发布  2：已更新但未再次发布 3：下线")
    private Integer status;
    @ApiModelProperty("分组id")
    private Long folderId;
    @ApiModelProperty("分组名称")
    private String folderName;
    @ApiModelProperty("是否冒烟测试通过")
    private Boolean isPass;
}
