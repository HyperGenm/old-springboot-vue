package com.weiziplus.springboot.common.filter;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.utils.StringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 过滤器
 *
 * @author wanglongwei
 * @data 2019/4/24 9:49
 */
@Component
public class CorsFilter implements Filter {

    /**
     * 过滤器配置--重写父类过滤器
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
        //如果不需要跨域
        if (StringUtil.isBlank(originHeader)) {
            chain.doFilter(req, res);
            return;
        }
        //如果是swagger-ui.html
        String referer = request.getHeader(HttpHeaders.REFERER);
        String pattern = "^(http://|https://)([a-zA-Z0-9\\.\\:]+)(\\/swagger-ui.html)";
        boolean matches = Pattern.compile(pattern).matcher(referer).matches();
        if (matches) {
            chain.doFilter(req, res);
            return;
        }
        //配置可以跨域访问的域名
        String[] allowDomain = {"http://localhost:8088"};
        Set<String> allowedOrigins = new HashSet<>(Arrays.asList(allowDomain));
        if (!allowedOrigins.contains(originHeader)) {
            //如果域名不存在，直接返回404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setHeader("Access-Control-Allow-Origin", originHeader);
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Accept,Content-Type,Origin," + GlobalConfig.TOKEN);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(req, res);
    }
}