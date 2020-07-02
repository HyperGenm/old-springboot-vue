package com.weiziplus.springboot.common.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取请求设备信息
 *
 * @author wanglongwei
 * @date 2020/05/27 15/56
 */
@Slf4j
public class UserAgentUtils {

    /**
     * 根据http获取userAgent信息
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    /**
     * 获取操作系统对象
     *
     * @param userAgent
     * @return
     */
    private static OperatingSystem getOperatingSystem(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        return agent.getOperatingSystem();
    }


    /**
     * 获取os：Windows/ios/Android
     *
     * @param request
     * @return
     */
    public static String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOs(userAgent);
    }

    /**
     * 获取os：Windows/ios/Android
     *
     * @param userAgent
     * @return
     */
    public static String getOs(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getGroup().getName();
    }


    /**
     * 获取deviceType
     *
     * @param request
     * @return
     */
    public static String getDevicetype(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDevicetype(userAgent);
    }

    /**
     * 获取deviceType
     *
     * @param userAgent
     * @return
     */
    public static String getDevicetype(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getDeviceType().toString();
    }

    /**
     * 获取操作系统的名字
     *
     * @param request
     * @return
     */
    public static String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsName(userAgent);
    }

    /**
     * 获取操作系统的名字
     *
     * @param userAgent
     * @return
     */
    public static String getOsName(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getName();
    }


    /**
     * 获取device的生产厂家
     *
     * @param request
     */
    public static String getDeviceManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceManufacturer(userAgent);
    }

    /**
     * 获取device的生产厂家
     *
     * @param userAgent
     * @return
     */
    public static String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        return operatingSystem.getManufacturer().toString();
    }

    /**
     * 获取浏览器对象
     *
     * @param agent
     * @return
     */
    public static Browser getBrowser(String agent) {
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        return userAgent.getBrowser();
    }


    /**
     * 获取browser name
     *
     * @param request
     * @return
     */
    public static String getBorderName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderName(userAgent);
    }

    /**
     * 获取browser name
     *
     * @param userAgent
     * @return
     */
    public static String getBorderName(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getName();
    }


    /**
     * 获取浏览器的类型
     *
     * @param request
     * @return
     */
    public static String getBorderType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderType(userAgent);
    }

    /**
     * 获取浏览器的类型
     *
     * @param userAgent
     * @return
     */
    public static String getBorderType(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getBrowserType().getName();
    }

    /**
     * 获取浏览器组： CHROME、IE
     *
     * @param request
     * @return
     */
    public static String getBorderGroup(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderGroup(userAgent);
    }

    /**
     * 获取浏览器组： CHROME、IE
     *
     * @param userAgent
     * @return
     */
    public static String getBorderGroup(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getGroup().getName();
    }

    /**
     * 获取浏览器的生产厂商
     *
     * @param request
     * @return
     */
    public static String getBrowserManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserManufacturer(userAgent);
    }


    /**
     * 获取浏览器的生产厂商
     *
     * @param userAgent
     * @return
     */
    public static String getBrowserManufacturer(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getManufacturer().getName();
    }


    /**
     * 获取浏览器使用的渲染引擎
     *
     * @param request
     * @return
     */
    public static String getBorderRenderingEngine(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderRenderingEngine(userAgent);
    }

    /**
     * 获取浏览器使用的渲染引擎
     *
     * @param userAgent
     * @return
     */
    public static String getBorderRenderingEngine(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getRenderingEngine().name();
    }


    /**
     * 获取浏览器版本
     *
     * @param request
     * @return
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserVersion(userAgent);
    }

    /**
     * 获取浏览器版本
     *
     * @param userAgent
     * @return
     */
    public static String getBrowserVersion(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getVersion(userAgent).toString();
    }


    public static void main(String[] args) {
        String winUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36";
        System.out.println("浏览器组：" + getBorderGroup(winUserAgent));
        System.out.println("浏览器名字：" + getBorderName(winUserAgent));
        System.out.println("浏览器类型" + getBorderType(winUserAgent));
        System.out.println("浏览器生产商：" + getBrowserManufacturer(winUserAgent));
        System.out.println("浏览器版本：" + getBrowserVersion(winUserAgent));
        System.out.println("设备生产厂商:" + getDeviceManufacturer(winUserAgent));
        System.out.println("设备类型:" + getDevicetype(winUserAgent));
        System.out.println("设备操作系统：" + getOs(winUserAgent));
        System.out.println("操作系统的名字：" + getOsName(winUserAgent));
        System.out.println("操作系统浏览器的渲染引擎:" + getBorderRenderingEngine(winUserAgent));
    }

}
