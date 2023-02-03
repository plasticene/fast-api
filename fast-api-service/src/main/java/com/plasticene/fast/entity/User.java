package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.plasticene.boot.mybatis.core.handlers.EncryptTypeHandler;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 18:59
 */
@Data
@TableName(autoResultMap = true)
public class User extends BaseDO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;


    /**
     * 性别 0：男  1：女
     */
    private Integer gender;

    /**
     * 状态 0：启用中  -1：停用
     */
    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;



}
