package com.weiziplus.springboot.filter;

import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.util.ToolUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Cors过滤器
 * 过滤器
 *
 * @author wanglongwei
 * @data 2019/4/24 9:49
 */
@Component
public class CorsFilter implements Filter {

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
        //如果是swagger-ui.html直接放行
        String referer = request.getHeader(HttpHeaders.REFERER);
        String pattern = "^(http://|https://)([a-zA-Z0-9\\.\\:]+)(\\/swagger-ui.html)";
        if (ToolUtils.notBlank(referer) && Pattern.compile(pattern).matcher(referer).matches()) {
            chain.doFilter(req, res);
            return;
        }
        //当前域名是否存在允许跨域域名内
        if (!GlobalConfig.CORS_FILTER_ORIGINS.contains(originHeader)) {
            //如果域名不存在，返回403拒绝访问
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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