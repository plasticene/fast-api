package com.plasticene.fast.dto;

import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/6 13:58
 */
@Data
public class FolderDTO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("分组名称")
    private String name;
    @ApiModelProperty("分组类型")
    private Integer type;


}
