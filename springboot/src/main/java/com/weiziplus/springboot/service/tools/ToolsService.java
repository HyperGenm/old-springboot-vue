package com.weiziplus.springboot.service.tools;

import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.utils.FileUtil;
import com.weiziplus.springboot.utils.ResultUtil;
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
    public ResultUtil upload(MultipartFile file) {
        String path = FileUtil.upFile(file, "文件上传");
        if (null == path) {
            return ResultUtil.error("文件上传失败，请重试");
        }
        return ResultUtil.success("success", path);
    }

}
