package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import com.plasticene.fast.dto.Parameter;
import com.plasticene.fast.handlers.JsonListTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 18:22
 */
@Data
@TableName(autoResultMap = true)
public class ApiInfo extends BaseDO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String path;
    private Integer type;
    private Long dataSourceId;
    private String databaseName;
    private String sqlContent;
    @TableField(typeHandler = JsonListTypeHandler.class)
    private List<Parameter> param;
    private String response;
    private Date releaseTime;

    // 0:草稿  1：发布  2：已更新但未再次发布 3：下线
    private Integer status;
    private Long folderId;

}
