package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import com.plasticene.fast.dto.Parameter;
import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 10:25
 */
@Data
public class ApiRelease extends BaseDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String path;
    private Integer type;
    private Long dataSourceId;
    private String databaseName;
    private String sqlContent;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Parameter> param;
    private String response;
    private Long apiInfoId;
}
