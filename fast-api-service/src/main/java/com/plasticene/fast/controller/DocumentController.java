package com.plasticene.fast.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/10 16:59
 */
@RestController
@RequestMapping("/api/docs")
@Api(tags = "生成接口文档管理")
public class DocumentController {

    @GetMapping
    public Object doc() {
        String s = "{\n" +
                "  \"swagger\": \"2.0\",\n" +
                "  \"info\": {\n" +
                "    \"description\": \"快速生成接口\",\n" +
                "    \"version\": \"1.0.0\",\n" +
                "    \"title\": \"fast-api\",\n" +
                "    \"contact\": {\n" +
                "      \"name\": \"shepherd\",\n" +
                "      \"url\": \"http://www.shepherd126.top/\",\n" +
                "      \"email\": \"shepherd_zfj163.com\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"host\": \"127.0.0.1:18888\",\n" +
                "  \"basePath\": \"/\",\n" +
                "  \"schemes\": [],\n" +
                "  \"consumes\": [\n" +
                "    \"*/*\"\n" +
                "  ],\n" +
                "  \"produces\": [\n" +
                "    \"*/*\"\n" +
                "  ],\n" +
                "  \"paths\": {\n" +
                "    \"/fds/dataSource\": {\n" +
                "      \"get\": {\n" +
                "        \"tags\": [\n" +
                "          \"数据源管理\"\n" +
                "        ],\n" +
                "        \"summary\": \"查询数据源列表\",\n" +
                "        \"operationId\": \"getListUsingGET\",\n" +
                "        \"produces\": [\n" +
                "          \"*/*\"\n" +
                "        ],\n" +
                "        \"parameters\": [\n" +
                "          {\n" +
                "            \"name\": \"name\",\n" +
                "            \"in\": \"query\",\n" +
                "            \"description\": \"数据源名称\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"pageNo\",\n" +
                "            \"in\": \"query\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"integer\",\n" +
                "            \"format\": \"int32\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"pageSize\",\n" +
                "            \"in\": \"query\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"integer\",\n" +
                "            \"format\": \"int32\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"status\",\n" +
                "            \"in\": \"query\",\n" +
                "            \"description\": \"状态：0：关闭  1：开启\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"integer\",\n" +
                "            \"format\": \"int32\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"type\",\n" +
                "            \"in\": \"query\",\n" +
                "            \"description\": \"类型  0：MySQL 1：clickhouse  2：doris\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"integer\",\n" +
                "            \"format\": \"int32\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"responses\": {\n" +
                "          \"200\": {\n" +
                "            \"description\": \"OK\",\n" +
                "            \"schema\": {\n" +
                "              \"$ref\": \"#/definitions/PageResult«DataSourceDTO»\",\n" +
                "              \"originalRef\": \"PageResult«DataSourceDTO»\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"401\": {\n" +
                "            \"description\": \"Unauthorized\"\n" +
                "          },\n" +
                "          \"403\": {\n" +
                "            \"description\": \"Forbidden\"\n" +
                "          },\n" +
                "          \"404\": {\n" +
                "            \"description\": \"Not Found\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"definitions\": {\n" +
                "    \"PageResult«DataSourceDTO»\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"list\": {\n" +
                "          \"type\": \"array\",\n" +
                "          \"items\": {\n" +
                "            \"$ref\": \"#/definitions/DataSourceDTO\",\n" +
                "            \"originalRef\": \"DataSourceDTO\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"pages\": {\n" +
                "          \"type\": \"integer\",\n" +
                "          \"format\": \"int64\"\n" +
                "        },\n" +
                "        \"total\": {\n" +
                "          \"type\": \"integer\",\n" +
                "          \"format\": \"int64\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"title\": \"PageResult«DataSourceDTO»\"\n" +
                "    },\n" +
                "    \"DataSourceDTO\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"createTime\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"format\": \"date-time\",\n" +
                "          \"description\": \"创建时间\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"databaseList\": {\n" +
                "          \"type\": \"array\",\n" +
                "          \"description\": \"数据库列表\",\n" +
                "          \"uniqueItems\": true,\n" +
                "          \"items\": {\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          \"refType\": \"string\"\n" +
                "        },\n" +
                "        \"host\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"地址\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"id\": {\n" +
                "          \"type\": \"integer\",\n" +
                "          \"format\": \"int64\",\n" +
                "          \"description\": \"主键\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"name\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"数据源名称\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"password\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"密码\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"port\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"端口号\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"selectDatabase\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"选择的数据库\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"status\": {\n" +
                "          \"type\": \"integer\",\n" +
                "          \"format\": \"int32\",\n" +
                "          \"description\": \"状态  0：关闭  1：启用\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"type\": {\n" +
                "          \"type\": \"integer\",\n" +
                "          \"format\": \"int32\",\n" +
                "          \"description\": \"类型  0：MySQL 1：clickhouse  2：doris\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"updateTime\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"format\": \"date-time\",\n" +
                "          \"description\": \"更新时间\",\n" +
                "          \"refType\": null\n" +
                "        },\n" +
                "        \"userName\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"用户名\",\n" +
                "          \"refType\": null\n" +
                "        }\n" +
                "      },\n" +
                "      \"title\": \"DataSourceDTO\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        return JSONObject.toJSON(s);
    }
}
