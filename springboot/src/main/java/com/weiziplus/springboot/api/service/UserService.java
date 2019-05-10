package com.weiziplus.springboot.api.service;

import com.weiziplus.springboot.api.mapper.UserMapper;
import com.weiziplus.springboot.common.utils.ResponseBean;
import com.weiziplus.springboot.common.utils.token.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:10
 */
@Service
public class UserService {
    @Autowired
    UserMapper mapper;

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public Map<String, Object> getInfo(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        return ResponseBean.success(mapper.getUserInfoByUserId(userId));
    }
}
