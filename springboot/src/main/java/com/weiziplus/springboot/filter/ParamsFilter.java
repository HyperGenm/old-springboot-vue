package com.weiziplus.springboot.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 参数过滤器
 *
 * @author wanglongwei
 * @data 2019/6/26 9:49
 */
@Component
public class ParamsFilter implements Filter {

    /**
     * 允许的url---该请求将不会过滤参数
     */
    private final List<String> ALLOW_URL = Arrays.asList("", "");

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestUrl = request.getRequestURI();
        //放过允许的url
        if (ALLOW_URL.contains(requestUrl)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        System.out.println(requestUrl);
        filterChain.doFilter(new ParamsHttpServletRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }

    static class ParamsHttpServletRequestWrapper extends HttpServletRequestWrapper {

        ParamsHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            String header = super.getHeader(name);
            if (null == header) {
                return null;
            }
            return HtmlUtils.htmlEscape(header);
        }

        @Override
        public String getParameter(String name) {
            String parameter = super.getParameter(name);
            if (null == parameter) {
                return null;
            }
            //去除左右的空格
            parameter = parameter.trim();
            //替换sql关键字
            parameter = cleanSqlKeyWords(parameter);
            return HtmlUtils.htmlEscape(parameter);
        }

        /**
         * 过滤参数值
         *
         * @param name
         * @return
         */
        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (null == values) {
                return null;
            }
            for (int i = 0; i < values.length; i++) {
                //值为空，跳过
                if (null == values[i]) {
                    continue;
                }
                //去除左右的空格
                values[i] = values[i].trim();
                //替换sql关键字
                values[i] = cleanSqlKeyWords(values[i]);
            }
            return values;
        }

        /**
         * 不允许的sql关键字
         */
        private static final String[] NOT_ALLOW_KEY_WORDS = {"and", "exec", "insert", "select", "delete", "update", "count"
                , "*", "%", "chr", "mid", "master", "truncate", "char", "declare", ";", "or", "-", "+"};

        /**
         * 如果传入的值有关键字，并且左右有空格---将sql关键字替换为INVALID
         *
         * @param value
         * @return
         */
        private String cleanSqlKeyWords(String value) {
            String space = " ";
            for (String keyword : NOT_ALLOW_KEY_WORDS) {
                if (value.contains(space + keyword) || value.contains(keyword + space)
                        || value.contains(space + keyword.toUpperCase()) || value.contains(keyword.toUpperCase() + space)) {
                    value = value.replace(keyword, "INVALID");
                }
            }
            return value;
        }
    }
}