package com.weiziplus.springboot.pc.system.service;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.utils.Md5Util;
import com.weiziplus.springboot.common.utils.ResponseBean;
import com.weiziplus.springboot.common.utils.StringUtil;
import com.weiziplus.springboot.common.utils.token.AdminTokenUtil;
import com.weiziplus.springboot.common.utils.token.JwtTokenUtil;
import com.weiziplus.springboot.pc.system.mapper.SysFunctionMapper;
import com.weiziplus.springboot.pc.system.mapper.SysRoleMapper;
import com.weiziplus.springboot.pc.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:08
 */
@Slf4j
@Service
public class AdminLoginService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysFunctionMapper sysFunctionMapper;

    @Autowired
    SysFunctionService sysFunctionService;

    /**
     * 系统用户登录
     *
     * @param username
     * @param password
     * @return
     */
    public Map<String, Object> login(String username, String password) {
        if (StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
            return ResponseBean.error("用户名或密码为空");
        }
        SysUser sysUser = sysUserMapper.getInfoByUsername(username);
        try {
            if (null == sysUser || !sysUser.getPassword().equals(Md5Util.encode(password))) {
                return ResponseBean.error("用户名或密码错误");
            }
        } catch (UnsupportedEncodingException e) {
            log.warn("系统用户登录MD5加密出错" + e);
            return ResponseBean.error("未知错误，请重试");
        }
        if (!GlobalConfig.ALLOW_LOGIN.equals(sysUser.getAllowLogin())) {
            return ResponseBean.error("账号被禁用，请联系管理员");
        }
        SysRole sysRole = sysRoleMapper.getInfoByUserId(sysUser.getId());
        if (null == sysRole) {
            return ResponseBean.error("您还没有角色，请联系管理员添加");
        }
        if (!GlobalConfig.IS_STOP.equals(sysRole.getIsStop())) {
            return ResponseBean.error("角色被禁用，请联系管理员");
        }
        String token = AdminTokenUtil.createToken(sysUser.getId());
        if (null == token) {
            return ResponseBean.error("登录失败，请重试");
        }
        Map<String, Object> map = new HashMap<>(1);
        map.put("token", token);
        map.put("userInfo", sysUser);
        map.put("role", sysRole.getName());
        map.put("routerTree", sysFunctionService.getMenuTreeByRoleId(sysRole.getId()));
        map.put("roleButtons", sysFunctionMapper.getButtonListByRoleId(sysRole.getId()));
        return ResponseBean.success(map);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    public Map<String, Object> logout(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        AdminTokenUtil.deleteToken(userId);
        return ResponseBean.success();
    }
}
