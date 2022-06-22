package com.shepherd.fast.cache;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/22 10:26
 */
//@Configuration
@ConfigurationProperties(prefix = "multilevel.cache")
@Data
public class MultilevelCacheProperties {

    private Integer accessExpireTime;

    private Double maxCapacityRate;

    private Double initRate;

}
