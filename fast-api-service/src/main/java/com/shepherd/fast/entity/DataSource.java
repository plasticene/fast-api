package com.shepherd.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/15 18:00
 */

@Data
public class DataSource implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String host;
    private String port;
    private String databaseName;
    private String userName;
    private String password;
    private Integer type;
    private Integer isDelete;
    private Date createTime;
    private Date updateTime;
}
