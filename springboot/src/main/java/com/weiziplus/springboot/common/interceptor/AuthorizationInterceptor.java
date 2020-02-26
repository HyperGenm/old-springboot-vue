package com.weiziplus.springboot.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.weiziplus.springboot.common.async.SystemAsync;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysLog;
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
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //如果有忽略token注解
        if (null != handlerMethod.getBeanType().getAnnotation(AuthTokenIgnore.class) || null != method.getAnnotation(AuthTokenIgnore.class)) {
            return true;
        }
        //检查class或者方法是否有@AdminAuthToken和@WebAuthToken，没有的话跳过拦截
        AdminAuthToken adminAuthTokenClass = handlerMethod.getBeanType().getAnnotation(AdminAuthToken.class);
        AdminAuthToken adminAuthTokenMethod = method.getAnnotation(AdminAuthToken.class);
        WebAuthToken webAuthTokenClass = handlerMethod.getBeanType().getAnnotation(WebAuthToken.class);
        WebAuthToken webAuthTokenMethod = method.getAnnotation(WebAuthToken.class);
        if (null == adminAuthTokenClass && null == adminAuthTokenMethod && null == webAuthTokenClass && null == webAuthTokenMethod) {
            return true;
        }
        //获取头部的token
        String token = request.getHeader(GlobalConfig.TOKEN);
        if (ToolUtils.isBlank(token)) {
            handleResponse(response, ResultUtils.errorToken("token不存在"));
            return false;
        }
        try {
            //判断jwtToken是否过期
            if (JwtTokenUtils.isExpiration(token)) {
                handleResponse(response, ResultUtils.errorToken("token失效"));
                return false;
            }
        } catch (Exception e) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //获取token中存放的issuer
        String issuer = JwtTokenUtils.getIssuer(token);
        //获取当前访问的ip地址
        String ipAddress = HttpRequestUtils.getIpAddress(request);
        if (!issuer.equals(ipAddress)) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
        //获取当前角色
        String tokenAudience = JwtTokenUtils.getUserAudienceByToken(token);
        if (AdminTokenUtils.AUDIENCE.equals(tokenAudience)) {
            //角色为admin
            //查看是否有日志注解，有的话将日志信息放入数据库
            SystemLog systemLog = method.getAnnotation(SystemLog.class);
            if (null != systemLog) {
                //查看是否存在忽略参数
                String paramIgnore = systemLog.paramIgnore();
                Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
                //使用迭代器的remove()方法删除元素
                parameterMap.keySet().removeIf(paramIgnore::contains);
                SysLog sysLog = new SysLog()
                        .setUserId(AdminTokenUtils.getUserIdByToken(token))
                        .setDescription(systemLog.description())
                        .setParam(JSON.toJSONString(parameterMap))
                        .setType(systemLog.type())
                        .setIpAddress(HttpRequestUtils.getIpAddress(request));
                //将日志异步放入数据库
                systemAsync.handleSysLog(sysLog);
            }
            return handleAdminToken(request, response, token, adminAuthTokenClass, adminAuthTokenMethod);
        } else if (WebTokenUtils.AUDIENCE.equals(tokenAudience)) {
            //角色为web
            return handleWebToken(response, token, webAuthTokenClass, webAuthTokenMethod);
        }
        //没有角色
        handleResponse(response, ResultUtils.errorToken("token不存在"));
        return false;
    }

    /**
     * 处理admin的token
     *
     * @param response
     * @param token
     * @param authTokenClass
     * @param authTokenMethod
     * @return
     */
    private boolean handleAdminToken(HttpServletRequest request, HttpServletResponse response, String token, AdminAuthToken authTokenClass, AdminAuthToken authTokenMethod) {
        //获取用户id
        Long userId = AdminTokenUtils.getUserIdByToken(token);
        //判断当前注解是否和当前角色匹配
        if (null == authTokenClass && null == authTokenMethod) {
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
     * @param response
     * @param token
     * @param authTokenClass
     * @param authTokenMethod
     * @return
     */
    private boolean handleWebToken(HttpServletResponse response, String token, WebAuthToken authTokenClass, WebAuthToken authTokenMethod) {
        //获取用户id
        Long userId = WebTokenUtils.getUserIdByToken(token);
        //判断当前注解是否和当前角色匹配
        if (null == authTokenClass && null == authTokenMethod) {
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
