package com.plasticene.fast.query;

import lombok.Data;


/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 14:42
 */
@Data
public class BaseQuery {
    private Integer pageSize = 20;
    private Integer pageNo = 1;
}
