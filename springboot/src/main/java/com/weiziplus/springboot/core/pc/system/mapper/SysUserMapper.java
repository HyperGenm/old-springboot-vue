package com.weiziplus.springboot.core.pc.system.mapper;

import com.weiziplus.springboot.core.pc.system.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/9 11:24
 */
@Mapper
public interface SysUserMapper {

    /**
     * 获取系统用户列表
     *
     * @param userName
     * @param roleId
     * @param allowLogin
     * @param lastActiveTime
     * @param createTime
     * @return
     */
    List<SysUserVo> getListVo(@Param("userName") String userName, @Param("roleId") Long roleId,
                                @Param("allowLogin") Integer allowLogin, @Param("lastActiveTime") String lastActiveTime,
                                @Param("createTime") String createTime);
}
