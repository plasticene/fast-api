package com.plasticene.fast.dto;

import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/21 14:38
 */
@Data
public class Column {
    private Integer index;
    private String columnName;
    private String dataType;
    private String columnType;
    private String columnComment;
}
