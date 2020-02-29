package com.weiziplus.springboot.core.pc.tools.service;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.util.FileUtils;
import com.weiziplus.springboot.common.util.HttpRequestUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author wanglongwei
 * @date 2019/8/24 16:27
 */
@Slf4j
@Service
public class ToolsService extends BaseService {
    @Resource
    ResourceLoader resourceLoader;

    /**
     * 文件上传
     *
     * @return
     */
    public ResultUtils upload(MultipartFile file) {
        String path = FileUtils.upFile(file);
        if (null == path) {
            return ResultUtils.error("文件上传失败，请重试");
        }
        return ResultUtils.success(new HashMap<String, String>(1) {{
            put("path", path);
        }});
    }

    /**
     * 文件下载
     */
    public void downTemp(HttpServletResponse response) {
        String path = "/logo.jpg";
        try {
            FileUtils.downFile(response, path);
        } catch (IOException e) {
            log.warn("文件下载失败,详情:" + e);
            HttpRequestUtils.handleErrorResponse(
                    response, ResultUtils.error("文件下载失败，详情:" + e), "文件下载");
        }
    }
}
