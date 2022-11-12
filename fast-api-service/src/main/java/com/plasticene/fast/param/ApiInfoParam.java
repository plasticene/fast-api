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
    @NotBlank
    private String name;
    @ApiModelProperty("api路径")
    @NotBlank
    private String path;
    @ApiModelProperty("api类型 0：get  1：post")
    @NotNull
    private Integer type;
    @ApiModelProperty("数据源id")
    @NotNull
    private Long dataSourceId;
    @ApiModelProperty("数据库")
    @NotBlank
    private String databaseName;
    @ApiModelProperty("SQL语句")
    @NotBlank
    private String sqlContent;
    @ApiModelProperty("api参数")
    private List<Parameter> param;
    @ApiModelProperty("api响应结果")
    private String response;
    @ApiModelProperty("api分组")
    @NotNull
    private Long folderId;
}
