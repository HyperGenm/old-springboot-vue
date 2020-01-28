package com.weiziplus.springboot.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

    public static String post(String url, List<NameValuePair> params) {
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build();
        try {
            HttpPost post = new HttpPost(url);
            if (null != params) {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
                post.setEntity(urlEncodedFormEntity);
            }
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            CloseableHttpResponse response = client.execute(post);
            if (HttpServletResponse.SC_OK != response.getStatusLine().getStatusCode()) {
                log.warn("网络请求出错:url" + url + "---状态码:" + response.getStatusLine().getStatusCode() + "---详情:" + response);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (null == entity) {
                return null;
            }
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            log.warn("请求出错" + e);
            return null;
        }
    }

    public static String post(String url) {
        return post(url, null);
    }

    public static String get(String url, Map<String, String> params) {
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
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (null == entity) {
                return null;
            }
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            log.warn("请求出错" + e);
            return null;
        }
    }

    public static String get(String url) {
        return get(url, null);
    }
}
