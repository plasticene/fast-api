package com.shepherd.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 14:30
 */
@Data
public class DataSourceQuery extends BaseQuery{
    @ApiModelProperty("数据源名称")
    private String name;
    @ApiModelProperty("类型  0：MySQL 1：clickhouse  2：doris")
    private Integer type;
}
