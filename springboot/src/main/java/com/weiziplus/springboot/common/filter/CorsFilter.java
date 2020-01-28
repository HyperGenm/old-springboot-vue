package com.weiziplus.springboot.common.filter;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.ToolUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cors过滤器
 * 过滤器
 *
 * @author wanglongwei
 * @date 2019/4/24 9:49
 */
@Component
public class CorsFilter implements Filter {

    /**
     * 可以跨域访问的地址
     */
    private static String[] CORS_FILTER_ORIGINS = {};

    @Value("${global.cors-filter-origins:http://localhost}")
    private void setCorsFilterOrigins(String corsFilterOrigins) {
        if (ToolUtils.isBlank(corsFilterOrigins)) {
            return;
        }
        CorsFilter.CORS_FILTER_ORIGINS = corsFilterOrigins.trim().split(",");
    }

    /**
     * 配置跨域
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String originHeader = request.getHeader(HttpHeaders.ORIGIN);
        //如果不需要跨域直接放行
        if (ToolUtils.isBlank(originHeader)) {
            chain.doFilter(req, res);
            return;
        }
        boolean isAllow = false;
        //当前地址是否在允许的地址中
        for (String origin : CORS_FILTER_ORIGINS) {
            //如果*,所有请求都允许
            if ("*".equals(origin)) {
                isAllow = true;
                break;
            }
            if (0 == originHeader.indexOf(origin)) {
                isAllow = true;
                break;
            }
        }
        //是否允许
        if (!isAllow) {
            //如果域名不存在，返回403拒绝访问
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET,PUT,OPTIONS,DELETE");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Accept,Content-Type,Origin," + GlobalConfig.TOKEN);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        chain.doFilter(req, res);
    }
}