package com.plasticene.fast.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 14:12
 */
@Data
public class Parameter {
    @ApiModelProperty("参数名")
    private String name;
    @ApiModelProperty("参数的位置，可能的值有：query、header、path、body。")
    private String in;
    @ApiModelProperty("是否必传 0：否  1：是")
    private Boolean required;
    @ApiModelProperty("参数类型：string, number,boolean, array")
    private String type;
    @ApiModelProperty("如果type是array，则必填。用于描述array里面的item")
    private Map<String,String> items;
    @ApiModelProperty("参数描述")
    private String description;
    @ApiModelProperty("value")
    private String value;
}
