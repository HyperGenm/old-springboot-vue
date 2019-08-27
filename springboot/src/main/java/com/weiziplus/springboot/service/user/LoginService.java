package com.weiziplus.springboot.service.user;

import com.github.pagehelper.util.StringUtil;
import com.weiziplus.springboot.mapper.user.UserMapper;
import com.weiziplus.springboot.models.User;
import com.weiziplus.springboot.util.Md5Utils;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.util.ValidateUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
import com.weiziplus.springboot.util.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:02
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public ResultUtils login(String username, String password) {
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            return ResultUtils.error("用户名或密码为空");
        }
        User user = userMapper.getUserInfoByUsername(username);
        if (null == user) {
            return ResultUtils.error("用户名或密码错误");
        }
        if (!user.getPassword().equals(Md5Utils.encode(password))) {
            return ResultUtils.error("用户名或密码错误");
        }
        Map<String, Object> resMap = new HashMap<>(1);
        String token = WebTokenUtils.createToken(user.getId());
        resMap.put("token", token);
        return ResultUtils.success(resMap);
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    public ResultUtils register(String username, String password) {
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            return ResultUtils.error("用户名或密码为空");
        }
        if (ValidateUtils.notUsername(username)) {
            return ResultUtils.error("用户名不能包含特殊字符");
        }
        if (ValidateUtils.notPassword(password)) {
            return ResultUtils.error("密码为6-20位大小写和数字");
        }
        User user = userMapper.getUserInfoByUsername(username);
        if (null != user) {
            return ResultUtils.error("用户名已存在");
        }
        String md5Pwd = Md5Utils.encode(password);
        return ResultUtils.success(userMapper.addUser(username, md5Pwd));
    }

    /**
     * 退出
     *
     * @return
     */
    public ResultUtils logout(HttpServletRequest request) {
        Long userId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        WebTokenUtils.deleteToken(userId);
        return ResultUtils.success();
    }
}
