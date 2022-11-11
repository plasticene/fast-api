package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.plasticene.boot.mybatis.core.metadata.BaseDO;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 10:28
 */
@Data
public class SqlQuery extends BaseDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long dataSourceId;
    private String databaseName;
    private String sqlContent;
    private String sqlLog;
    private Long folderId;
}
