package com.plasticene.fast.filter;

import com.plasticene.boot.common.constant.OrderConstant;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 10:50
 */
@Configuration
public class FilterConfig {

    @Resource
    private AuthFilter authFilter;

    @Bean
    public FilterRegistrationBean buildAuthFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(OrderConstant.FILTER_TRACE + 10);
        filterRegistrationBean.setFilter(authFilter);
        filterRegistrationBean.addUrlPatterns("/fds/*");
        return filterRegistrationBean;
    }
}
