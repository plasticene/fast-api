package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.plasticene.fast.global.JsonStringSetTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/15 18:00
 */

@Data
@TableName(autoResultMap = true)
public class DataSource extends BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String host;
    private String port;
    @TableField(typeHandler = JsonStringSetTypeHandler.class)
    private Set<String> databaseList;
    private String userName;
    private String password;
    private Integer type;
}
