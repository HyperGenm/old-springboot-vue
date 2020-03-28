package com.weiziplus.springboot;

import com.alibaba.fastjson.JSON;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.enums.UserAllowLogin;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.models.User;
import com.weiziplus.springboot.core.api.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests extends BaseService {

    @Autowired
    UserMapper mapper;

    @Test
    public void test() {
        List<SysUser> sysUsers = baseFindListByClassAndColumnAndValueMap(SysUser.class, new HashMap<String, Object>() {{
            put(SysUser.COLUMN_ICON, "/logo.jpg");
            put(SysUser.COLUMN_ALLOW_LOGIN, UserAllowLogin.ALLOW.getValue());
            put(SysUser.COLUMN_ROLE_ID, 666);
        }});
        System.out.println(sysUsers);
    }

}
