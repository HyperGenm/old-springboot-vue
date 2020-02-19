package com.weiziplus.springboot.core.api.user.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.util.token.WebTokenUtils;
import com.weiziplus.springboot.core.api.user.mapper.UserMapper;
import com.weiziplus.springboot.common.models.User;
import com.weiziplus.springboot.common.util.DateUtils;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.common.util.ToolUtils;
import com.weiziplus.springboot.common.util.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/10 17:10
 */
@Service
public class UserService extends BaseService {

    @Autowired
    UserMapper mapper;

    /**
     * UserService基础redis的key
     */
    private static final String BASE_REDIS_KEY = "api:user:service:UserService:";

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public ResultUtils<User> getInfo(HttpServletRequest request) {
        Long userId = WebTokenUtils.getUserIdByHttpServletRequest(request);
        return ResultUtils.success(baseFindByClassAndId(User.class, userId));
    }

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtils<PageUtils<List<User>>> getUserList(Integer pageNum, Integer pageSize) {
        //模拟redis使用
        String key = createRedisKey(BASE_REDIS_KEY + "getUserList", pageNum, pageSize);
        Object object = RedisUtils.get(key);
        if (null != object) {
            List<User> userList = ToolUtils.objectOfList(object, User.class);
            return ResultUtils.success(PageUtils.pageInfo(userList));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = baseFindAllByClass(User.class);
        RedisUtils.set(key, userList);
        PageUtils<List<User>> pageUtil = PageUtils.pageInfo(userList);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 模拟删除redis
     * 删除两次防止redis与数据库内容不一致
     */
    public ResultUtils addUser() {
        //通过默认过期时间删除redis
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        User user = new User()
                .setUsername(ToolUtils.createUUID().substring(0, 5))
                .setPassword(ToolUtils.createUUID().substring(10))
                .setCreateTime(DateUtils.getNowDateTime());
        baseInsert(user);
        //删除redis
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }
}
