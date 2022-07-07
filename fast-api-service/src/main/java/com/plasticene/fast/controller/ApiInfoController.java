package com.plasticene.fast.controller;

import com.plasticene.fast.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 17:25
 */
@ResponseResultBody
@RestController
@RequestMapping("/api/info")
@Api(tags = "api管理")
public class ApiInfoController {
}
