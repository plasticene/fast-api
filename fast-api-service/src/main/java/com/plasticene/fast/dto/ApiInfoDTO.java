package com.plasticene.fast.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/11 10:52
 */
@Data
public class ApiInfoDTO {
    private Long id;
    private String name;
    private String path;
    private Integer type;
    private Long dataSourceId;
    private String databaseName;
    private String sqlContent;
    private String param;
    private String response;
    private Date releaseTime;

    // 0:草稿  1：冒烟测试通过  2：发布  3：已更新但未再次发布 4：下线
    private Integer status;
    private Long folderId;
}
