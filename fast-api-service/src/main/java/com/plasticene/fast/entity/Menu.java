package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/31 16:40
 */
@Data
public class Menu extends BaseDO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 访问后端接口权限标识
     */
    private String code;

    /**
     * 父级菜单id
     */
    private Long parentId;

    /**
     * 路径
     */
    private String path;

    /**
     * 图标
     */
    private String icon;

    /**
     * 组件
     */
    private String component;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 类型 0：菜单 1：按钮
     */
    private Integer type;

    private Integer status;
}
