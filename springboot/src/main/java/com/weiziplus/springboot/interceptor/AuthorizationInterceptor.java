package com.weiziplus.springboot.interceptor;

import com.alibaba.fastjson.JSON;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.system.SysLogMapper;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.mapper.user.UserMapper;
import com.weiziplus.springboot.models.User;
import com.weiziplus.springboot.service.system.SysFunctionService;
import com.weiziplus.springboot.service.system.SysRoleService;
import com.weiziplus.springboot.util.HttpRequestUtils;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.util.ToolUtils;
import com.weiziplus.springboot.util.redis.StringRedisUtils;
import com.weiziplus.springboot.util.token.AdminTokenUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
import com.weiziplus.springboot.util.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * 自定义的拦截器interceptor
 *
 * @author wanglongwei
 * @data 2019/4/22 16:08
 */
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SysLogMapper sysLogMapper;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysFunctionService sysFunctionService;

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
                sysLogMapper.addSysLog(JwtTokenUtils.getUserIdByToken(token), systemLog.description(), HttpRequestUtils.getIpAddress(request));
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
        Long userId = JwtTokenUtils.getUserIdByToken(token);
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
        //更新用户最后活跃时间
        int i = sysUserMapper.updateLastActiveTimeByIdAndIp(userId, HttpRequestUtils.getIpAddress(request));
        //如果更新成功，证明有该用户，反之没有该用户
        if (0 >= i) {
            handleResponse(response, ResultUtils.errorToken("token失效"));
            return false;
        }
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
        Long roleId = JwtTokenUtils.getRoleIdByToken(token);
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
        Long userId = JwtTokenUtils.getUserIdByToken(token);
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
        //查看是否存在该用户
        User user = userMapper.getUserInfoByUserId(userId);
        if (null == user) {
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
        PrintWriter out = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=utf-8");
            out = response.getWriter();
            out.print(JSON.toJSONString(errResult));
        } catch (Exception e) {
            log.warn("token失效输入到前端页面出错，catch" + e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}
