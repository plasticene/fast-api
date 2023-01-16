package com.plasticene.fast.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.plasticene.boot.common.exception.BizException;
import com.plasticene.boot.common.user.LoginUser;
import com.plasticene.boot.common.utils.PtcBeanUtils;
import com.plasticene.fast.constant.UserConstant;
import com.plasticene.fast.entity.User;
import com.plasticene.fast.param.LoginParam;
import com.plasticene.fast.service.AuthService;
import com.plasticene.fast.service.UserService;
import com.plasticene.fast.vo.LoginVO;
import org.apache.commons.io.FileUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 18:57
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public LoginVO login(LoginParam param) {
        Integer type = param.getType();
        String username = param.getUsername();
        String password = param.getPassword();
        User user = userService.getUserByUsername(username);
        if (user == null || !Objects.equals(user.getPassword(), password)) {
            throw new BizException("账号或密码不正确");
        }
        String token = IdUtil.fastSimpleUUID();
        LoginUser loginUser = PtcBeanUtils.copy(user, LoginUser.class);
        loginUser.setToken(token);
        LoginVO loginVO = PtcBeanUtils.copy(user, LoginVO.class);
        loginVO.setToken(token);
        stringRedisTemplate.opsForValue().set(UserConstant.LOGIN_KEY_PREFIX + token,
                JSON.toJSONString(loginUser), 2, TimeUnit.HOURS);
        return loginVO;
    }

}
