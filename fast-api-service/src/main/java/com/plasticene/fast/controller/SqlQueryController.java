package com.plasticene.fast.controller;

import com.plasticene.boot.web.core.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/11/7 09:53
 */
@RequestMapping("/sql/query")
@Api(tags = "SQL查询关联")
@RestController
@ResponseResultBody
public class SqlQueryController {
}
