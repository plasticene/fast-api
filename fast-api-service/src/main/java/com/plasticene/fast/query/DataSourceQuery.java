package com.plasticene.fast.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty("状态：0：关闭  1：开启")
    private Integer status;
}
