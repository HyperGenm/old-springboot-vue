package com.weiziplus.springboot.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.weiziplus.springboot.api.mapper.UserMapper;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.models.User;
import com.weiziplus.springboot.common.utils.ResultUtil;
import com.weiziplus.springboot.common.utils.StringUtil;
import com.weiziplus.springboot.common.utils.redis.StringRedisUtil;
import com.weiziplus.springboot.common.utils.token.AdminTokenUtil;
import com.weiziplus.springboot.common.utils.token.JwtTokenUtil;
import com.weiziplus.springboot.common.utils.token.WebTokenUtil;
import com.weiziplus.springboot.pc.system.mapper.SysLogMapper;
import com.weiziplus.springboot.pc.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
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
        if (StringUtil.isBlank(token)) {
            handleResponse(response, ResultUtil.errorToken("token不存在"));
            return false;
        }
        //判断jwtToken是否过期
        if (JwtTokenUtil.isExpiration(token)) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //获取角色
        switch (JwtTokenUtil.getUserAudienceByToken(token)) {
            //角色为admin
            case AdminTokenUtil.AUDIENCE: {
                //查看是否有日志注解，有的话将日志信息放入数据库
                SystemLog systemLog = method.getAnnotation(SystemLog.class);
                if (null != systemLog) {
                    sysLogMapper.addSysLog(JwtTokenUtil.getUserIdByToken(token), systemLog.description());
                }
                return handleAdminToken(response, token, adminAuthTokenClass, adminAuthTokenMethod);
            }
            //角色为web
            case WebTokenUtil.AUDIENCE: {
                return handleWebToken(response, token, webAuthTokenClass, webAuthTokenMethod);
            }
            default: {
                return false;
            }
        }
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
    private boolean handleAdminToken(HttpServletResponse response, String token, AdminAuthToken authTokenClass, AdminAuthToken authTokenMethod) {
        //获取用户id
        Long userId = JwtTokenUtil.getUserIdByToken(token);
        //判断当前注解是否和当前角色匹配
        if (null == authTokenClass && null == authTokenMethod) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //查看redis是否过期
        if (!StringRedisUtil.hasKye(AdminTokenUtil.getAudienceRedisKey(userId))) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!StringRedisUtil.get(AdminTokenUtil.getAudienceRedisKey(userId)).equals(token)) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //查看是否存在该用户
        SysUser sysUser = sysUserMapper.getInfoById(userId);
        if (null == sysUser) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //更新用户最后活跃时间
        sysUserMapper.updateLastActiveTimeById(userId);
        //更新token过期时间
        AdminTokenUtil.updateExpireTime(userId);
        return true;
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
        Long userId = JwtTokenUtil.getUserIdByToken(token);
        //判断当前注解是否和当前角色匹配
        if (null == authTokenClass && null == authTokenMethod) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //查看redis是否过期
        if (!StringRedisUtil.hasKye(WebTokenUtil.getAudienceRedisKey(userId))) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!StringRedisUtil.get(WebTokenUtil.getAudienceRedisKey(userId)).equals(token)) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //查看是否存在该用户
        User user = userMapper.getUserInfoByUserId(userId);
        if (null == user) {
            handleResponse(response, ResultUtil.errorToken("token失效"));
            return false;
        }
        //更新token过期时间
        WebTokenUtil.updateExpireTime(userId);
        return true;
    }

    /**
     * 将token出错信息输入到前端页面
     *
     * @param response
     * @param errResult
     */
    private void handleResponse(HttpServletResponse response, ResultUtil errResult) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
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
