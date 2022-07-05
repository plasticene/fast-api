package com.shepherd.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/4 18:07
 */
@Data
public class FolderParam {

    @ApiModelProperty(value = "分组名称")
    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称长度最大不能超过50个字符")
    private String name;
}
