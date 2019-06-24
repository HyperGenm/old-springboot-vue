package com.weiziplus.springboot.config;

import com.weiziplus.springboot.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置过滤器、拦截器等
 *
 * @author wanglongwei
 * @data 2019/5/7 8:42
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 拦截器配置---重写父类方法
     * <p>
     * addPathPatterns  拦截所有请求
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");
    }

    /**
     * 自定义的拦截器interceptor
     *
     * @return
     */
    @Bean
    public AuthorizationInterceptor authenticationInterceptor() {
        return new AuthorizationInterceptor();
    }
}
