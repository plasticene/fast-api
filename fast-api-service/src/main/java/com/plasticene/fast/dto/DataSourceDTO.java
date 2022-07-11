package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 14:24
 */
@Data
public class DataSourceDTO {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("数据源名称")
    private String name;
    @ApiModelProperty("地址")
    private String host;
    @ApiModelProperty("端口号")
    private String port;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("数据库列表")
    private Set<String> databaseList;
    @ApiModelProperty("类型  0：MySQL 1：clickhouse  2：doris")
    private Integer type;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("选择的数据库")
    private String selectDatabase;


}
