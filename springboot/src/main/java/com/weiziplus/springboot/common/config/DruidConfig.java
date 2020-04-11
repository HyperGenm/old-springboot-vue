package com.weiziplus.springboot.common.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author wanglongwei
 * @date 2020/04/11 17/10
 */
@Component
public class DruidConfig {

    @Value("${spring.datasource.druid.username:root}")
    private String username;

    @Value("${spring.datasource.druid.password:weiziplus}")
    private String password;

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        // IP白名单 (没有配置或者为空，则允许所有访问)
        registrationBean.addInitParameter("allow", "");
        // IP黑名单 (存在共同时，deny优先于allow)
        registrationBean.addInitParameter("deny", "");
        registrationBean.addInitParameter("loginUsername", username);
        registrationBean.addInitParameter("loginPassword", password);
        //是否能够重置数据
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;
    }

}
