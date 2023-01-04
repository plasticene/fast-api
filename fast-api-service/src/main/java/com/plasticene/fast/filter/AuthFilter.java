package com.plasticene.fast.filter;

import com.alibaba.fastjson.JSON;
import com.plasticene.boot.common.enums.ResponseStatusEnum;
import com.plasticene.boot.common.pojo.ResponseVO;
import com.plasticene.boot.common.user.LoginUser;
import com.plasticene.boot.common.user.RequestUserHolder;
import com.plasticene.fast.constant.UserConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/3 22:27
 */
@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${excludeUrlPatterns}")
    private List<String> excludeUrlPatterns;


    private static final String TOKEN_KEY = "token";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 判断当前请求是否需要登录
            Boolean flag = ignoreAuthentication(request);
            if (flag) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = getRequestToken(request);
            response.setContentType("application/json;charset=UTF-8");
            if (StringUtils.isBlank(token)) {
                unauthorized(response);
                return;
            }

            String userInfo = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_KEY_PREFIX + token);
            if (StringUtils.isBlank(userInfo)) {
                unauthorized(response);
                return;
            }
            LoginUser loginUser = JSON.parseObject(userInfo, LoginUser.class);
            RequestUserHolder.add(loginUser);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("auth error:", e);
            response.getWriter().write(JSON.toJSONString(ResponseVO.failure(ResponseStatusEnum.BAD_REQUEST.getCode(),
                    ResponseStatusEnum.BAD_REQUEST.getMsg())));
            return;
        } finally {
            RequestUserHolder.remove();
        }
    }

    Boolean ignoreAuthentication(HttpServletRequest request) {
        String currentUrl = request.getMethod() + request.getServletPath();
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : excludeUrlPatterns) {
            if (matcher.match(pattern, currentUrl)) {
                return true;
            }
        }
        return false;
    }


    void unauthorized(HttpServletResponse response) throws IOException {
        response.getWriter().write(JSON.toJSONString(ResponseVO.failure(ResponseStatusEnum.UNAUTHORIZED.getCode(),
                ResponseStatusEnum.UNAUTHORIZED.getMsg())));
    }

    String getRequestToken(HttpServletRequest request) {
        // 从header中查找
        String token = request.getHeader(TOKEN_KEY);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // header中没有，从parameter请求参数中获取
        token = request.getParameter(TOKEN_KEY);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // header和parameter中没有，从cookie中获取
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), TOKEN_KEY)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

}