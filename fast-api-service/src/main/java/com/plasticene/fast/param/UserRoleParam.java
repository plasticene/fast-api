package com.plasticene.fast.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/2/3 14:35
 */
@Data
public class UserRoleParam {
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("所选角色id")
    private List<Long> roleIds;
}
