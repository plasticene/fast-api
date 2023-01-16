package com.plasticene.fast.global;

import com.alibaba.fastjson.JSON;
import com.plasticene.boot.common.enums.ResponseStatusEnum;
import com.plasticene.boot.common.pojo.ResponseVO;
import com.plasticene.boot.common.user.RequestUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/16 17:20
 */

/**
 * 访问一个需要认证的 URL 资源，已经认证（登录）但是没有权限的情况下，返回 {@link GlobalErrorCodeConstants#FORBIDDEN} 错误码。
 *
 * 补充：Spring Security 通过 {@link ExceptionTranslationFilter#handleAccessDeniedException(HttpServletRequest, HttpServletResponse, FilterChain, AccessDeniedException)} 方法，调用当前类
 *
 * @author 芋道源码
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException {
        // 打印 warn 的原因是，不定期合并 warn，看看有没恶意破坏
        log.warn("[commence][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(),
                RequestUserHolder.getCurrentUser().getId(), e);
        // 返回 403
        response.getWriter().write(JSON.toJSONString(ResponseVO.failure(ResponseStatusEnum.UNAUTHORIZED.getCode(),
                "权限不足")));
    }

}

