package com.weiziplus.springboot.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2019/8/5 8:56
 */
@Slf4j
public class HttpRequestUtils {

    /**
     * 获取用户ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (null != ip) {
                    ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
                }
            }
            return ip;
        }
        //ip地址最大长度为15
        int ipMaxNum = 15;
        if (ipMaxNum < ip.length()) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!(unknown.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * post请求
     * 私有方法，根据实际情况自定义公共方法
     *
     * @param url
     * @param paramsJsonStr
     * @return
     */
    private static ResultUtils<String> post(String url, String paramsJsonStr) {
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build();
        try {
            HttpPost post = new HttpPost(url);
            if (!ToolUtils.isBlank(paramsJsonStr)) {
                StringEntity stringEntity = new StringEntity(paramsJsonStr, StandardCharsets.UTF_8);
                stringEntity.setContentEncoding(StandardCharsets.UTF_8.name());
                stringEntity.setContentType("application/x-www-form-urlencoded;charset=utf-8");
                post.setEntity(stringEntity);
            }
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            CloseableHttpResponse response = client.execute(post);
            if (HttpServletResponse.SC_OK != response.getStatusLine().getStatusCode()) {
                log.warn("网络请求出错:url" + url + "---状态码:" + response.getStatusLine().getStatusCode() + "---详情:" + response);
                return ResultUtils.error("网络请求出错，详情:" + response);
            }
            HttpEntity entity = response.getEntity();
            if (null == entity) {
                return ResultUtils.error("网络请求出错，详情:" + response);
            }
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.warn("请求出错" + e);
            return ResultUtils.error("网络请求出错，详情:" + e);
        }
    }

    /**
     * post请求无参数
     *
     * @param url
     * @return
     */
    private static ResultUtils<String> post(String url) {
        return post(url, null);
    }

    /**
     * get请求
     *
     * @param url
     * @param params
     * @return
     */
    private static ResultUtils<String> get(String url, Map<String, String> params) {
        if (null != params) {
            StringBuilder stringBuilder = new StringBuilder(url);
            boolean flag = false;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                String key = entry.getKey();
                if (!flag) {
                    stringBuilder.append("?");
                    flag = true;
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append(key).append("=").append(value);
            }
            url = stringBuilder.toString();
        }
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build();
        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            CloseableHttpResponse response = client.execute(get);
            if (HttpServletResponse.SC_OK != response.getStatusLine().getStatusCode()) {
                log.warn("网络请求出错:url" + url + "---状态码:" + response.getStatusLine().getStatusCode() + "---详情:" + response);
                return ResultUtils.error("网络请求出错，详情:" + response);
            }
            HttpEntity entity = response.getEntity();
            if (null == entity) {
                return ResultUtils.error("网络请求出错，详情:" + response);
            }
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.warn("请求出错" + e);
            return ResultUtils.error("网络请求出错，详情:" + e);
        }
    }

    /**
     * get请求无参数
     *
     * @param url
     * @return
     */
    private static ResultUtils<String> get(String url) {
        return get(url, null);
    }

    /**
     * 将错误信息输出到前端页面
     *
     * @param response
     * @param errResult
     */
    public static void handleErrorResponse(HttpServletResponse response, ResultUtils errResult, String errMsg) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=utf-8");
            out = response.getWriter();
            out.print(JSON.toJSONString(errResult));
        } catch (Exception e) {
            log.warn(errMsg + ",输入到前端页面出错，详情:" + e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}

