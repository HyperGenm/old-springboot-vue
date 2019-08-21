package com.weiziplus.springboot.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 参数过滤器
 *
 * @author wanglongwei
 * @data 2019/6/26 9:49
 */
@Component
public class ParamsFilter implements Filter {

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
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
            return HtmlUtils.htmlEscape(parameter);
        }

        /**
         * 去掉字符串左右空格
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
                if (null == values[i]) {
                    continue;
                }
                values[i] = values[i].trim();
            }
            return values;
        }
    }
}