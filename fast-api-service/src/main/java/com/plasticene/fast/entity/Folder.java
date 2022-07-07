package com.plasticene.fast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 18:19
 */
@Data
public class Folder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long parentId;
    private Integer isDelete;

}
