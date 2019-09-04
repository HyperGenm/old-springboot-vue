package com.weiziplus.springboot.filter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
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
            //替换sql关键字--mybatis框架已经过滤大部分---如果需要请放开限制
//            parameter = cleanSqlKeyWords(parameter);
            //过滤html标签
            return JsoupUtil.clean(parameter);
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
                //替换sql关键字--mybatis框架已经过滤大部分---如果需要请放开限制
//                values[i] = cleanSqlKeyWords(values[i]);
                //过滤html标签
                values[i] = JsoupUtil.clean(values[i]);
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
                if (value.toLowerCase().contains(space + keyword) || value.toLowerCase().contains(keyword + space)) {
                    value = value.replace(keyword, "INVALID");
                }
            }
            return value;
        }
    }

    /**
     * Jsoup工具类
     */
    static class JsoupUtil {

        /**
         * none():清除所有HTML标签，仅保留文本节点。
         * simpleText():仅会保留b, em, i, strong, u 标签，除此之外的所有HTML标签都会被清除。
         * basic():保留 a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul 和其适当的属性标签，除此之外的所有HTML标签都会被清除，且该API不允许出现图片(img tag)。另外该API中允许出现的超链接中可以允许其指定http, https, ftp, mailto 且在超链接中强制追加rel=nofollow属性。
         * basicWithImages():保留basic()中允许出现的标签的同时也允许出现图片(img tag)和img的相关适当属性，且其src允许其指定 http 或 https。
         * relaxed():仅会保留 a, b, blockquote, br, caption, cite, code, col, colgroup, dd, div, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, span, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul 标签，除此之外的所有HTML标签都会被清除，且在超链接中不会强制追加rel=nofollow属性。
         */
        private static final Whitelist WHITELIST = Whitelist.basicWithImages();

        /**
         * 配置过滤化参数, 不对代码进行格式化
         */
        private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

        static {
            // 标签添加style属性
            WHITELIST.addAttributes(":all", "style");
        }

        /**
         * 过滤字符串
         *
         * @param content
         * @return
         */
        public static String clean(String content) {
            return Jsoup.clean(content, "", WHITELIST, OUTPUT_SETTINGS);
        }
    }
}