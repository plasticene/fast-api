package com.plasticene.fast.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/23 10:37
 */
@Data
public class CacheMessage implements Serializable {
    private String cacheName;
    private Object key;
    private Object value;
    private Integer type;
}
