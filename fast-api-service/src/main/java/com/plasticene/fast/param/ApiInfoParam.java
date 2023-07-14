package com.plasticene.fast.param;

import com.plasticene.fast.dto.Parameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 10:09
 */
@Data
public class ApiInfoParam {

    @ApiModelProperty("api名称")
    @NotBlank(message = "api名称不能为空")
    private String name;
    @ApiModelProperty("api路径")
    @NotBlank(message = "api路径不能为空")
    private String path;
    @ApiModelProperty("api类型 0：get  1：post")
    @NotNull(message = "api类型不能为空")
    private Integer type;
    @ApiModelProperty("数据源id")
    @NotNull(message = "数据源id不能为空")
    private Long dataSourceId;
    @ApiModelProperty("数据库")
    @NotBlank(message = "数据库不能为空")
    private String databaseName;
    @ApiModelProperty("SQL语句")
    @NotBlank(message = "sql语句不能为空")
    private String sqlContent;
    @ApiModelProperty("api参数")
    private List<Parameter> param;
    @ApiModelProperty("api响应结果")
    private String response;
    @ApiModelProperty("api分组")
    @NotNull(message = "分组不能为空")
    private Long folderId;
}
