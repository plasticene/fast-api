package com.plasticene.fast.entity;

import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/12/3 21:12
 */
@Data
public class Application extends BaseDO {
    private Long id;
    private String appId;
    private String appKey;
    private String appSecret;
    private String name;
    private String remark;
    private Integer isActive;
}
