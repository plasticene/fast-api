package com.plasticene.fast.config;

import com.google.common.collect.Multimap;
import com.plasticene.boot.web.core.filter.WebTraceFilter;
import com.plasticene.fast.global.AccessDeniedHandlerImpl;
import com.plasticene.fast.global.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/3 17:26
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {


    /**
     * 由于 Spring Security 创建 AuthenticationManager 对象时，没声明 @Bean 注解，导致无法被注入
     * 通过覆写父类的该方法，添加 @Bean 注解，解决该问题
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 权限不够处理器 Bean
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    /**
     * 认证失败处理类 Bean
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    /**
     * 配置 URL 的安全配置
     * <p>
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     *
     *
     *
     * 这里配置不认证所有接口，登录认证的逻辑保持在原有的登录过滤器即可
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity, AccessDeniedHandler accessDeniedHandler,
                                              AuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        httpSecurity
                // 开启跨域
                .cors().and()
                // CSRF 禁用，使用的是 Java 代码配置 spring Security，那么 CSRF 保护默认是开启的，
                // 那么在 POST 方式提交表单的时候就必须验证 Token，如果没有，那么自然也就是 403 没权限了。
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);



//        // 设置每个请求的权限
        httpSecurity
                // ①：全局共享规则
                .authorizeRequests()
                // 1.1 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/**/*.html", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers("/profile/**").anonymous()
                .antMatchers("/common/download**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/doc.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                .antMatchers("/fds/test/auth/admin").permitAll() // 所有用户可访问
                // 设置API无需认证
//                .antMatchers( "/**").permitAll()
                // ②：每个项目的自定义规则
                .and()
                // ③：兜底规则，必须认证
                .authorizeRequests()
                .anyRequest().authenticated();
//
//        // 添加 Token Filter
//        httpSecurity.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);


        // <X> 配置请求地址的权限
//        httpSecurity.authorizeRequests()
////            .antMatchers("/fds/test/auth").permitAll() // 所有用户可访问
////            .antMatchers("/fds/test/auth/admin").hasRole("ADMIN") // 需要 ADMIN 角色
////            .antMatchers("/fds/test/auth/normal").access("hasRole('ROLE_NORMAL')") // 需要 NORMAL 角色。
//            // 任何请求，访问的用户都需要经过认证
//            .anyRequest().authenticated()
//            // <Y> 设置 Form 表单登录
//            .and().formLogin();
        return httpSecurity.build();
    }


}
