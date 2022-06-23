package com.shepherd.fast.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 10:26
 */
@Component
@ConfigurationProperties(prefix = "multilevel.cache")
@Data
public class MultilevelCacheProperties {

    private Integer accessExpireTime;

    private Double maxCapacityRate;

    private Double initRate;

    private String topic;

    private String Name;

    private String redisName;

    private String caffeineName;

    private Integer redisExpireTime;

    private Integer caffeineExpireTime;


}
