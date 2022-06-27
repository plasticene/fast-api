package com.shepherd.fast.controller;

import com.shepherd.fast.anno.ResponseResultBody;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/24 17:27
 */
@ResponseResultBody
@RestController
@RequestMapping("/folder")
@Api(tags = "分组管理")
public class FolderController {
}
