package com.plasticene.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 11:52
 */
@Data
public class DataSourceParam {

    @ApiModelProperty("数据源名称")
    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称长度最大不能超过50个字符")
    private String name;
    @ApiModelProperty("地址")
    @NotBlank(message = "地址不能为空")
    private String host;
    @ApiModelProperty("端口号")
    @NotBlank(message = "端口号不能为空")
    private String port;
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty("数据库列表")
    @NotNull(message = "数据库不能为空")
    private Set<String> databaseList;
    @ApiModelProperty("类型  0：MySQL 1：clickhouse  2：doris")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
