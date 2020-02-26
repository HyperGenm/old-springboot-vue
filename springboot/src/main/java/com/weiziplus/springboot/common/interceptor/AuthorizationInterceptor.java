package com.weiziplus.springboot.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.weiziplus.springboot.common.async.SystemAsync;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysUserLog;
import com.weiziplus.springboot.common.util.HttpRequestUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.common.util.ToolUtils;
import com.weiziplus.springboot.common.util.redis.StringRedisUtils;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import com.weiziplus.springboot.common.util.token.JwtTokenUtils;
import com.weiziplus.springboot.common.util.token.WebTokenUtils;
import com.weiziplus.springboot.core.pc.system.service.SysFunctionService;
import com.weiziplus.springboot.core.pc.system.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义的拦截器interceptor
 *
 * @author wanglongwei
 * @date 2019/4/22 16:08
 */
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysFunctionService sysFunctionService;

    @Autowired
    SystemAsync systemAsync;

    /**
     * 处理请求的时间戳
     * 时间戳错误返回false
     *
     * @param request
     */
    private boolean handleTimeStamp(HttpServletRequest request) {
        String timeStamp = request.getParameter("__t");
        if (null == timeStamp || 0 >= timeStamp.length()) {
            return false;
        }
        long timeMillis = System.currentTimeMillis();
        int allowTime = 60000;
        //如果请求时间戳和服务器当前时间相差超过60秒，本次请求失败
        return allowTime > Math.abs(timeMillis - Long.valueOf(timeStamp));
    }

    /**
     * 判断token注解
     *
     * @param object
     * @return
     */
    private boolean handleTokenAnnotation(Object object) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //如果有忽略token注解
        if (null != handlerMethod.getBeanType().getAnnotation(AuthTokenIgnore.class)
                || null != method.getAnnotation(AuthTokenIgnore.class)) {
            return true;
        }
        //检查class或者方法是否有@AdminAuthToken和@WebAuthToken，没有的话跳过拦截
        AdminAuthToken adminAuthTokenClass = handlerMethod.getBeanType().getAnnotation(AdminAuthToken.class);
        AdminAuthToken adminAuthTokenMethod = method.getAnnotation(AdminAuthToken.class);
        WebAuthToken webAuthTokenClass = handlerMethod.getBeanType().getAnnotation(WebAuthToken.class);
        WebAuthToken webAuthTokenMethod = method.getAnnotation(WebAuthToken.class);
        return null == adminAuthTokenClass && null == adminAuthTokenMethod
                && null == webAuthTokenClass && null == webAuthTokenMethod;
    }

    /**
     * 判断token是否过期
     *
     * @param request
     * @return
     */
    private boolean handleJwtTokenNotExpiration(HttpServletRequest request) {
        //获取头部的token
        String token = request.getHeader(GlobalConfig.TOKEN);
        if (ToolUtils.isBlank(token)) {
            return false;
        }
        try {
            //判断jwtToken是否过期
            if (JwtTokenUtils.isExpiration(token)) {
                return false;
            }
        } catch (Exception e) {
            log.warn("拦截器判断jwtToken是否过期出错，详情:" + e);
            return false;
        }
        //获取token中存放的issuer
        String issuer = JwtTokenUtils.getIssuer(token);
        //获取当前访问的ip地址
        String ipAddress = HttpRequestUtils.getIpAddress(request);
        return issuer.equals(ipAddress);
    }

    /**
     * 请求之前拦截
     *
     * @param request
     * @param response
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //判断时间戳有效
        if (!handleTimeStamp(request)) {
            handleResponse(response, ResultUtils.error("时间戳错误"));
            return false;
        }
        //判断是否存在token注解
        if (handleTokenAnnotation(object)) {
            return true;
        }
        //判断请求头中token是否失效
        if (!handleJwtTokenNotExpiration(request)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //获取token
        String token = request.getHeader(GlobalConfig.TOKEN);
        //获取当前角色
        String tokenAudience = JwtTokenUtils.getUserAudienceByToken(token);
        //角色为admin
        if (AdminTokenUtils.AUDIENCE.equals(tokenAudience)) {
            return handleAdminToken(request, response, object);
        }
        //角色为web
        if (WebTokenUtils.AUDIENCE.equals(tokenAudience)) {
            return handleWebToken(request, response, object);
        }
        //没有角色
        handleResponse(response, ResultUtils.errorToken("token不存在"));
        return false;
    }

    /**
     * 处理admin的token
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleAdminToken(HttpServletRequest request, HttpServletResponse response, Object object) {
        //获取token
        String token = request.getHeader(GlobalConfig.TOKEN);
        //获取用户id
        Long userId = AdminTokenUtils.getUserIdByToken(token);
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //判断当前注解是否和当前角色匹配
        if (null == handlerMethod.getBeanType().getAnnotation(AdminAuthToken.class)
                && null == method.getAnnotation(AdminAuthToken.class)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //查看redis是否过期
        if (!StringRedisUtils.hasKye(AdminTokenUtils.getAudienceRedisKey(userId))) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!StringRedisUtils.get(AdminTokenUtils.getAudienceRedisKey(userId)).equals(token)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        ////////////token验证成功
        //查看是否有日志注解，有的话将日志信息放入数据库
        com.weiziplus.springboot.common.interceptor.SysUserLog systemLog = method.getAnnotation(com.weiziplus.springboot.common.interceptor.SysUserLog.class);
        if (null != systemLog) {
            //查看是否存在忽略参数
            String paramIgnore = systemLog.paramIgnore();
            Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
            //使用迭代器的remove()方法删除元素
            parameterMap.keySet().removeIf(paramIgnore::contains);
            SysUserLog sysLog = new SysUserLog()
                    .setUserId(userId)
                    .setDescription(systemLog.description())
                    .setParam(JSON.toJSONString(parameterMap))
                    .setType(systemLog.type())
                    .setIpAddress(HttpRequestUtils.getIpAddress(request));
            //将日志异步放入数据库
            systemAsync.handleSysLog(sysLog);
        }
        //异步更新用户最后活跃时间
        systemAsync.updateLastActiveTime(userId, HttpRequestUtils.getIpAddress(request));
        //如果当前是超级管理员---直接放过
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            //更新token过期时间
            AdminTokenUtils.updateExpireTime(userId);
            return true;
        }
        //获取当前访问的url
        String requestUrl = request.getRequestURI();
        Set<String> allFunContainApi = sysFunctionService.getAllFunContainApi();
        //如果限制的功能api不包含当前url---直接放过
        if (null == allFunContainApi || !allFunContainApi.contains(requestUrl)) {
            //更新token过期时间
            AdminTokenUtils.updateExpireTime(userId);
            return true;
        }
        //获取roleId
        Integer roleId = AdminTokenUtils.getRoleIdByToken(token);
        if (null == roleId) {
            handleResponse(response, ResultUtils.errorRole("您没有权限"));
            return false;
        }
        //获取当前角色拥有的方法url
        Set<String> funContainApiByRoleId = sysFunctionService.getFunContainApiByRoleId(roleId);
        if (null != funContainApiByRoleId && funContainApiByRoleId.contains(requestUrl)) {
            //更新token过期时间
            AdminTokenUtils.updateExpireTime(userId);
            return true;
        }
        handleResponse(response, ResultUtils.errorRole("您没有权限"));
        return false;
    }

    /**
     * 处理web的token
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleWebToken(HttpServletRequest request, HttpServletResponse response, Object object) {
        //获取token
        String token = request.getHeader(GlobalConfig.TOKEN);
        //获取用户id
        Long userId = WebTokenUtils.getUserIdByToken(token);
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //判断当前注解是否和当前角色匹配
        if (null == handlerMethod.getBeanType().getAnnotation(WebAuthToken.class)
                && null == method.getAnnotation(WebAuthToken.class)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //查看redis是否过期
        if (!StringRedisUtils.hasKye(WebTokenUtils.getAudienceRedisKey(userId))) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!StringRedisUtils.get(WebTokenUtils.getAudienceRedisKey(userId)).equals(token)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //更新token过期时间
        WebTokenUtils.updateExpireTime(userId);
        return true;
    }

    /**
     * 将token出错信息输入到前端页面
     *
     * @param response
     * @param errResult
     */
    private void handleResponse(HttpServletResponse response, ResultUtils errResult) {
        HttpRequestUtils.handleErrorResponse(response, errResult, "token出错");
    }

}
