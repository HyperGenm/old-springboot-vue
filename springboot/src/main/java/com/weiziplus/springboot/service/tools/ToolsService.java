package com.weiziplus.springboot.service.tools;

import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.util.FileUtils;
import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wanglongwei
 * @data 2019/8/24 16:27
 */
@Service
public class ToolsService extends BaseService {

    /**
     * 文件上传
     *
     * @return
     */
    public ResultUtils upload(MultipartFile file) {
        String path = FileUtils.upFile(file, "文件上传");
        if (null == path) {
            return ResultUtils.error("文件上传失败，请重试");
        }
        return ResultUtils.success("success", path);
    }

}