package com.shepherd.fast.vo;

import com.shepherd.fast.dto.Column;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/21 16:14
 */
@Data
public class DataResultVO {

    private List<Column> head;

    private List<Map<String, Object>> content;

    private Integer total;

    private Integer pages;

    private Integer size;

    private Integer current;

    public DataResultVO() {
    }
    public DataResultVO(List<Column> head, List<Map<String, Object>> content) {
        this.head = head;
        this.content = content;
    }

    public DataResultVO(List<Column> head, List<Map<String, Object>> content, Integer total, Integer pages, Integer size, Integer current) {
        this.head = head;
        this.content = content;
        this.total = total;
        this.pages = pages;
        this.size = size;
        this.current = current;
    }
}
