package com.shepherd.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 18:22
 */
@Data
public class ApiInfo {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String path;
    private Integer type;
    private Long dataSourceId;
    private String databaseName;
    private String sql;
    private String param;
    private String response;
    private Integer isDelete;
    private Date releaseTime;
    private Date createTime;
    private Date updateTime;

}
