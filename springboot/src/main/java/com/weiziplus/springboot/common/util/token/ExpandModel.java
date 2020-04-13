package com.weiziplus.springboot.common.util.token;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * jwt中存放的自定义内容
 *
 * @author wanglongwei
 * @date 2020/04/13 20/03
 */
@Data
@Accessors(chain = true)
public class ExpandModel {

    private Integer roleId;

}
