package com.plasticene.fast.dto;

import lombok.Data;

import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/29 14:26
 */
@Data
public class ServerInfo {
    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的MAC地址
     */
    private String macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    private String systemUuid;
}
