package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/29 15:21
 */
@Data
public class Role extends BaseDO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 是否系统内置 0：否  1：是
     */
    private Integer isSystem;
    /**
     * 备注
     */
    private String remark;
}
