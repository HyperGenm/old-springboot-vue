package com.weiziplus.springboot.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 参数过滤器
 *
 * @author wanglongwei
 * @data 2019/6/26 9:49
 */
@Component
public class ParamsFilter implements Filter {

    /**
     * 去掉字符串参数左右空格
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (null == value) {
                    continue;
                }
                values[i] = value.trim();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}