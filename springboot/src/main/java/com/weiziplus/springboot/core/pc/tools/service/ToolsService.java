package com.weiziplus.springboot.core.pc.tools.service;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.util.FileUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @author wanglongwei
 * @date 2019/8/24 16:27
 */
@Service
public class ToolsService extends BaseService {

    /**
     * 文件上传
     *
     * @return
     */
    public ResultUtils upload(MultipartFile file) {
        String path = FileUtils.upFilePc(file, "文件上传");
        if (null == path) {
            return ResultUtils.error("文件上传失败，请重试");
        }
        return ResultUtils.success(new HashMap<String, String>(1) {{
            put("path", path);
        }});
    }

}
